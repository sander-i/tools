package org.tools.bedrock.util;

import org.tools.bedrock.constant.SpecialSymbolConsts;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sander
 * @version 1.0
 */
public class JsonUtils {

    public static String listToJSON(List<?> list) {
        if (BaseUtils.isEmpty(list)) return null;
        StringBuilder result = new StringBuilder(SpecialSymbolConsts.LEFT_BRACKET);
        int size = 1;
        for (Object o : list) {
            if (o instanceof List) {
                result.append(listToJSON((List<?>) o));
            } else if (o instanceof Map) {
                result.append(mapToJSON((Map<?, ?>) o));
            } else {
                result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(o).append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
            }
            if (size++ < list.size()) {
                result.append(SpecialSymbolConsts.SEMICOLON);
            }
        }
        result.append(SpecialSymbolConsts.RIGHT_BRACKET);
        return result.toString();
    }

    public static String mapToJSON(Map<?, ?> map) {
        if (BaseUtils.isEmpty(map)) return null;
        StringBuilder result = new StringBuilder(SpecialSymbolConsts.LEFT_BRACE);
        int size = 1;
        String key;
        Object val;
        List<?> valList;
        Map<?, ?> valMap;
        Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
        for (Map.Entry<?, ?> entry : entries) {
            key = String.valueOf(entry.getKey());
            result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(key).append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(SpecialSymbolConsts.COLON);

            val = entry.getValue();

            if (val instanceof List) {
                valList = (List<?>) val;
                result.append(listToJSON(valList));
            } else if (val instanceof Map) {
                valMap = (Map<?, ?>) val;
                result.append(mapToJSON(valMap));
            } else if (val.getClass().isArray()) {
                Object[] array = (Object[]) val;
                result.append(SpecialSymbolConsts.LEFT_BRACKET);
                for (Object o : array) {
                    result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(o).append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
                }
                result.append(SpecialSymbolConsts.RIGHT_BRACKET);
            } else {
                result.append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK).append(val).append(SpecialSymbolConsts.DOUBLE_QUOTATION_MARK);
            }
            if (size++ < map.size()) {
                result.append(SpecialSymbolConsts.SEMICOLON);
            }
        }
        result.append(SpecialSymbolConsts.RIGHT_BRACE);
        return result.toString();
    }

}
