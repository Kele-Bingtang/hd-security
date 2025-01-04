package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdBasicAuthHelper;

import java.util.function.Supplier;

/**
 * 函数式接口：创建 HdBasicAuthHelper 的策略
 * <pre>
 *     返回：HdBasicAuthHelper
 * </pre>
 *
 * @author Tianke
 * @date 2025/1/3 22:59:11
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdBasicAuthHelperCreateSupplier extends Supplier<HdBasicAuthHelper> {
}
