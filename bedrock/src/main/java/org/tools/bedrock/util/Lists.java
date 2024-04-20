package org.tools.bedrock.util;


import org.tools.bedrock.constant.Constants;

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
public class Lists {

    private Lists() {
    }

    /**
     * List为 空
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * List非 空
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean isNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * List 多条记录
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean multiRecord(List<?> list) {
        return list.size() > Constants.ONE_I;
    }

    /**
     * List 记录是否满足传入条数
     *
     * @param list List<?>
     * @param size 大小
     * @return boolean
     */
    public static boolean otherRecord(List<?> list, int size) {
        return list.size() == size;
    }

    /**
     * List 一条记录
     *
     * @param list List<?>
     * @return boolean
     */
    public static boolean oneRecord(List<?> list) {
        return otherRecord(list, Constants.ONE_I);
    }

    /**
     * ArrayList 集合反转
     * jdk1.8新特性 stream流
     *
     * @param list List<T>
     * @return List<T>
     */
    public static <T> List<T> reversalStream(List<T> list) {
        return list.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
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

    /**
     * 集合分页
     *
     * @param list 集合
     * @param rows 行数
     * @param <T>  泛型
     * @return List<List < T>>
     */
    public static <T> List<List<T>> pagination(List<T> list, int rows) {
        List<List<T>> gather = new ArrayList<>();
        if (BaseUtils.isEmpty(list)) {
            return gather;
        }
        if (rows <= Constants.ZERO_I || list.size() <= rows) {
            gather.add(list);
            return gather;
        }
        // 集合大小/行数 页数
        int page = list.size() / rows;
        int start;
        int end = 0;
        for (int i = 0; i <= page; i++) {
            start = i * rows;
            // 行数1 出现一个空集填充
            if (start == list.size()) {
                break;
            }
            end = (end += rows) > list.size() ? list.size() : end;
            gather.add(list.subList(start, end));

        }
        return gather;
    }

}
