package org.tools.bedrock.util;

import java.util.function.Supplier;

/**
 * <p>
 * description
 * </p>
 *
 * @author sander 2024/08/04
 */
public class FeatureUtil {

    /**
     * 特性切换
     *
     * @param supplier       线上内容
     * @param returnSupplier 沙箱内容
     * @param toggle         切换
     * @param <T>            T
     * @return 线上内容 ｜ 沙箱内容
     */
    public static <T> T featureToggle(Supplier<T> supplier, Supplier<T> returnSupplier, boolean toggle) {
        return toggle ? returnSupplier.get() : supplier.get();
    }

}
