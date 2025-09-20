package org.tools.bedrock.base.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点类，用于表示图像中的坐标点
 *
 * @author sander
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Point {
    private int x;
    private int y;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}