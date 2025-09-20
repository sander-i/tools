package org.tools.bedrock.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Lists 冒烟测试
 * 测试基本的List操作功能
 */
public class ListsTest {

    @Test
    public void testIsEmpty() {
        // 测试isEmpty方法
        assertTrue("null列表应为空", Lists.isEmpty(null));
        assertTrue("空列表应为空", Lists.isEmpty(new ArrayList<>()));
        assertFalse("非空列表不应为空", Lists.isEmpty(Arrays.asList("item")));
    }

    @Test
    public void testIsNotEmpty() {
        // 测试isNotEmpty方法
        assertFalse("null列表应为空", Lists.isNotEmpty(null));
        assertFalse("空列表应为空", Lists.isNotEmpty(new ArrayList<>()));
        assertTrue("非空列表应为非空", Lists.isNotEmpty(Arrays.asList("item")));
    }

    @Test
    public void testOneRecord() {
        // 测试oneRecord方法
        assertFalse("空列表不应为一条记录", Lists.oneRecord(new ArrayList<>()));
        assertTrue("单元素列表应为一条记录", Lists.oneRecord(Arrays.asList("item")));
        assertFalse("多元素列表不应为一条记录", Lists.oneRecord(Arrays.asList("item1", "item2")));
    }

    @Test
    public void testMultiRecord() {
        // 测试multiRecord方法
        assertFalse("空列表不应为多条记录", Lists.multiRecord(new ArrayList<>()));
        assertFalse("单元素列表不应为多条记录", Lists.multiRecord(Arrays.asList("item")));
        assertTrue("多元素列表应为多条记录", Lists.multiRecord(Arrays.asList("item1", "item2")));
    }

    @Test
    public void testOtherRecord() {
        // 测试otherRecord方法
        List<String> list = Arrays.asList("item1", "item2", "item3");
        assertTrue("列表大小匹配时应返回true", Lists.otherRecord(list, 3));
        assertFalse("列表大小不匹配时应返回false", Lists.otherRecord(list, 2));
    }

    @Test
    public void testReversalStream() {
        // 测试reversalStream方法
        List<Integer> original = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> reversed = Lists.reversalStream(original);

        assertNotNull("反转后的列表不应为null", reversed);
        assertEquals("反转后的列表大小应相同", original.size(), reversed.size());
        assertEquals("第一个元素应为原列表最后一个元素", Integer.valueOf(5), reversed.get(0));
        assertEquals("最后一个元素应为原列表第一个元素", Integer.valueOf(1), reversed.get(reversed.size() - 1));
    }

    @Test
    public void testExtractGenericListStream() {
        // 测试extractGenericListStream方法
        List<Object> mixedList = new ArrayList<>();
        mixedList.add("string1");
        mixedList.add(123);
        mixedList.add("string2");
        mixedList.add(45.6);

        List<String> stringList = Lists.extractGenericListStream(mixedList, String.class);

        assertNotNull("提取后的列表不应为null", stringList);
        assertEquals("应提取出2个字符串", 2, stringList.size());
        assertTrue("应包含string1", stringList.contains("string1"));
        assertTrue("应包含string2", stringList.contains("string2"));
    }

    @Test
    public void testPagination() {
        // 测试pagination方法
        List<Integer> original = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 测试正常分页
        List<List<Integer>> pages = Lists.pagination(original, 3);
        assertNotNull("分页结果不应为null", pages);
        assertEquals("应分为4页", 4, pages.size());
        assertEquals("第一页应有3个元素", 3, pages.get(0).size());
        assertEquals("第二页应有3个元素", 3, pages.get(1).size());
        assertEquals("第三页应有3个元素", 3, pages.get(2).size());
        assertEquals("第四页应有1个元素", 1, pages.get(3).size());

        // 测试行数大于列表大小的情况
        List<List<Integer>> singlePage = Lists.pagination(original, 15);
        assertNotNull("分页结果不应为null", singlePage);
        assertEquals("应只有1页", 1, singlePage.size());
        assertEquals("该页应包含所有元素", 10, singlePage.get(0).size());

        // 测试空列表分页
        List<List<Integer>> emptyPages = Lists.pagination(new ArrayList<>(), 3);
        assertNotNull("分页结果不应为null", emptyPages);
        assertTrue("空列表分页应为空", emptyPages.isEmpty());

        // 测试无效行数
        List<List<Integer>> invalidRows = Lists.pagination(original, 0);
        assertNotNull("分页结果不应为null", invalidRows);
        assertEquals("无效行数应返回单页", 1, invalidRows.size());
        assertEquals("该页应包含所有元素", 10, invalidRows.get(0).size());
    }

    @Test
    public void testEdgeCases() {
        // 测试边界情况

        // 测试null列表分页
        List<List<String>> nullPagination = Lists.pagination(null, 5);
        assertNotNull("null列表分页结果不应为null", nullPagination);
        assertTrue("null列表分页应为空", nullPagination.isEmpty());

        // 测试null列表提取
        List<String> nullExtract = Lists.extractGenericListStream(null, String.class);
        assertNotNull("null列表提取结果不应为null", nullExtract);
        assertTrue("null列表提取应为空列表", nullExtract.isEmpty());

        // 测试null列表反转
        List<String> nullReversed = Lists.reversalStream(null);
        assertNotNull("null列表反转应返回空集合", nullReversed);
    }
}
