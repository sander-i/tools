/**
 * 基础数据对象的 JDK 序列化变体包。
 *
 * <p>与 {@link org.tools.bedrock.base} 下的同名对象保持字段结构一致，
 * 差异仅在于实现 {@link java.io.Serializable}（含 serialVersionUID），
 * 供 RPC、缓存等 JDK 序列化场景使用。枚举（Sex/Nation/TimeSeason）
 * 天生可序列化，直接复用基础包定义，本包不重复提供。</p>
 *
 * @author sander
 * @since 1.0.4
 */
package org.tools.bedrock.base.serializable;
