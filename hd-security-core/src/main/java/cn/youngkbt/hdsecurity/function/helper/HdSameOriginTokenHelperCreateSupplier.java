package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdSameOriginTokenHelper;

import java.util.function.Supplier;

/**
 * @author Tianke
 * @date 2025/1/3 22:52:37
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSameOriginTokenHelperCreateSupplier extends Supplier<HdSameOriginTokenHelper> {
}
