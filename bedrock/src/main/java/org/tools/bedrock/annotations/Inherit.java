package org.tools.bedrock.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

/**
 * @author sander
 * @version 1.0
 * @date 2023/02/19 0:18
 * @description 继承私有属性
 * @scope all
 */
@Inherited  // 可以被继承
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Inherit {

}
