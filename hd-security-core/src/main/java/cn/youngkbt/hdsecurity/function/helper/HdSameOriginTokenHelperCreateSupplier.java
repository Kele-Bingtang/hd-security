package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdSameOriginTokenHelper;

import java.util.function.Supplier;

/**
 * 函数式接口：创建 HdSameOriginTokenHelper 的策略
 * <pre>
 *     返回：HdSameOriginTokenHelper
 * </pre>
 *
 * @author Tianke
 * @date 2025/1/3 22:52:37
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSameOriginTokenHelperCreateSupplier extends Supplier<HdSameOriginTokenHelper> {
}
