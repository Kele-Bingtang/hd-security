package cn.youngkbt.hdsecurity.annotation;

/**
 * 注解鉴权的验证模式
 *
 * @author Tianke
 * @date 2024/12/20 00:43:52
 * @since 1.0.0
 */
public enum HdMode {
    /**
     * 必须具有所有的元素
     */
    AND,

    /**
     * 只需具有其中一个元素
     */
    OR
}
