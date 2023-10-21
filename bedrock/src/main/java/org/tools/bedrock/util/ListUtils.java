package org.tools.bedrock.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:10
 * @description List<T> 工具类
 * @scope all
 */
public class ListUtils {

    private ListUtils() {
    }

    /**
     * ArrayList 数组反转
     * <p>
     * Collections.reverse(list);
     * </p>
     *
     * @param list List<T>
     * @return List<T>
     */
    public static <T> List<T> arrayListReversal(List<T> list) {
        List<T> reversal = new ArrayList<>();
        for (int i = list.size() - 1; i > -1; i--) {
            reversal.add(list.get(i));
        }
        return reversal;
    }

    /**
     * ArrayList 数组反转
     * jdk1.8新特性 stream流
     *
     * @param list List<T>
     * @return List<T>
     */
    public static <T> List<T> arrayListReversalStream(List<T> list) {
        return list.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * 提取list
     *
     * @param list list集合
     * @param clz  提取泛型class
     * @param <T>  泛型
     * @return List<T>
     */
    public static <T> List<T> extractGenericList(List<T> list, Class<T> clz) {
        List<T> extract = new ArrayList<>();
        if (BaseUtils.isNotEmpty(list, clz)) {
            list.forEach(o -> {
                if (clz.getTypeName().equals(o.getClass().getTypeName())) {
                    extract.add(o);
                }
            });
        }
        return extract;
    }

    /**
     * 提取list
     * jdk1.8新特性 stream流
     *
     * @param list list集合
     * @param clz  提取泛型class
     * @param <T>  泛型
     * @return List<T>
     */
    public static <T> List<T> extractGenericListStream(List<T> list, Class<T> clz) {
        return BaseUtils.isNotEmpty(list, clz) ?
                list.stream()
                        .filter(o -> clz.getTypeName().equals(o.getClass().getTypeName()))
                        .collect(Collectors.toList())
                : new ArrayList<>();
    }

}
