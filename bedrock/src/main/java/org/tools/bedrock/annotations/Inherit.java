package org.tools.bedrock.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

/**
 * 可继承标记注解 @Inherit，运行时保留，供子类继承父类上的注解。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.0
 */
@Inherited  // 可以被继承
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Inherit {
}
