package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdAnnotationHelper;

import java.util.function.Supplier;

/**
 * 函数式接口：创建 HdAnnotationHelper 的策略
 * <pre>
 *     返回：HdAnnotationHelper
 * </pre>
 *
 * @author Tianke
 * @date 2025/1/3 22:51:09
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdAnnotationHelperCreateSupplier extends Supplier<HdAnnotationHelper> {
}
