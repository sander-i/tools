package org.tools.bedrock.util;

import org.tools.bedrock.constant.v2.SpecialSymbolConst;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sander
 * @version 1.0
 */
public class JsonUtil {

    /**
     * list集合转json
     *
     * @param list 集合
     * @return json
     * @deprecated JsonUtil.listToJson
     */
    public static String listToJSON(List<?> list) {
        return listToJson(list);
    }

    /**
     * map集合转json
     *
     * @param map 集合
     * @return json
     * @deprecated JsonUtil.mapToJson
     */
    public static String mapToJSON(Map<?, ?> map) {
        return mapToJson(map);
    }

    /**
     * list集合转json
     *
     * @param list 集合
     * @return json
     */
    public static String listToJson(List<?> list) {
        return listToJson(list, false);
    }

    /**
     * list集合转json
     *
     * @param list     集合
     * @param skipNull 跳过null值
     * @return json
     */
    public static String listToJson(List<?> list, boolean skipNull) {
        if (BaseUtils.isEmpty(list)) {
            return null;
        }
        StringBuilder result = new StringBuilder(SpecialSymbolConst.LEFT_BRACKET);
        int size = 1;
        for (Object o : list) {
            if (skipNull && BaseUtils.isNull(o)) {
                continue;
            }

            if (o instanceof List) {
                result.append(listToJson((List<?>) o, skipNull));
            } else if (o instanceof Map) {
                result.append(mapToJson((Map<?, ?>) o, skipNull));
            } else {
                result.append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK).append(o).append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK);
            }
            if (size++ < list.size()) {
                result.append(SpecialSymbolConst.SEMICOLON);
            }
        }
        result.append(SpecialSymbolConst.RIGHT_BRACKET);
        return result.toString();
    }

    /**
     * map集合转json
     *
     * @param map 集合
     * @return json
     */
    public static String mapToJson(Map<?, ?> map) {
        return mapToJson(map, false);
    }

    /**
     * map集合转json
     *
     * @param map      集合
     * @param skipNull 跳过null值
     * @return json
     */
    public static String mapToJson(Map<?, ?> map, boolean skipNull) {
        if (BaseUtils.isEmpty(map)) {
            return null;
        }
        StringBuilder result = new StringBuilder(SpecialSymbolConst.LEFT_BRACE);
        int size = 1;
        String key;
        Object val;
        List<?> valList;
        Map<?, ?> valMap;
        Set<? extends Map.Entry<?, ?>> entries = map.entrySet();
        for (Map.Entry<?, ?> entry : entries) {
            key = String.valueOf(entry.getKey());
            val = entry.getValue();
            if (skipNull && BaseUtils.isNull(val)) {
                continue;
            }

            result.append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK).append(key).append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK).append(SpecialSymbolConst.COLON);
            if (val instanceof List) {
                valList = (List<?>) val;
                result.append(listToJson(valList, skipNull));
            } else if (val instanceof Map) {
                valMap = (Map<?, ?>) val;
                result.append(mapToJson(valMap, skipNull));
            } else if (val.getClass().isArray()) {
                Object[] array = (Object[]) val;
                result.append(SpecialSymbolConst.LEFT_BRACKET);
                for (Object o : array) {
                    result.append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK).append(o).append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK);
                }
                result.append(SpecialSymbolConst.RIGHT_BRACKET);
            } else {
                result.append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK).append(val).append(SpecialSymbolConst.DOUBLE_QUOTATION_MARK);
            }
            if (size++ < map.size()) {
                result.append(SpecialSymbolConst.SEMICOLON);
            }
        }
        result.append(SpecialSymbolConst.RIGHT_BRACE);
        return result.toString();
    }


}
