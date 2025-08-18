package org.tools.bedrock.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sander
 * @version 1.0
 * @date 2025/08/01 18:16
 * @description 描述
 * @scope 作用域
 */
public class SudokuTest {

    int[] baseNumbers = new int[]{1, 2, 3, 4};

    @Test
    public void test() {
        // 组
        List<List<Integer>> groups = new ArrayList<>();

        // 行
        List<List<Integer>> rows = new ArrayList<>();

        List<Integer> row1 = new ArrayList<>();
        row1.add(4);
        row1.add(null);
        row1.add(1);
        row1.add(2);

        rows.add(row1);

        List<Integer> row2 = new ArrayList<>();
        row2.add(1);
        row2.add(null);
        row2.add(4);
        row2.add(null);

        rows.add(row2);

        List<Integer> row3 = new ArrayList<>();
        row3.add(2);
        row3.add(null);
        row3.add(3);
        row3.add(4);

        rows.add(row3);

        List<Integer> row4 = new ArrayList<>();
        row4.add(null);
        row4.add(4);
        row4.add(null);
        row4.add(1);

        rows.add(row4);

//        System.out.println(rows);

        // 列
        List<List<Integer>> columns = rowConvertColumn(rows);

        
    }

    private List<List<Integer>> rowConvertColumn(List<List<Integer>> rows) {
        List<List<Integer>> columns = new ArrayList<>();

        AtomicInteger currentColumn = new AtomicInteger();

        rows.forEach(row -> {
            List<Integer> column = new ArrayList<>();
            rows.forEach(r -> column.add(r.get(currentColumn.get())));
            currentColumn.getAndIncrement();
            columns.add(column);
        });

        return columns;
    }

}
