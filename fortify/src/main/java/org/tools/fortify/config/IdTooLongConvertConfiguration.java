package org.tools.fortify.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 解决Long类型ID过长无法解析的问题
 * </p>
 *
 * @author sander 2024/06/27
 */
@Component
public class IdTooLongConvertConfiguration {

    private static final long MAX = (long) Math.pow(2, 53) - 1;

    /**
     * 解决前端无法处理大数字丢失精度问题，将Long和BigInteger转换成string
     *
     * @return MappingJackson2HttpMessageConverter
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 序列换成json时,将所有的long变成string 因为js中得数字类型会转换成BigNumber2(5,14)
        simpleModule.addSerializer(Long.TYPE, LongToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, LongToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return jackson2HttpMessageConverter;
    }

    public static class LongToStringSerializer extends JsonSerializer<Long> {

        public static final LongToStringSerializer instance = new LongToStringSerializer();

        private LongToStringSerializer() {
        }

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                return;
            }

            if (value >= MAX) {
                gen.writeString(value.toString());
            } else {
                gen.writeNumber(value);
            }
        }
    }

}
