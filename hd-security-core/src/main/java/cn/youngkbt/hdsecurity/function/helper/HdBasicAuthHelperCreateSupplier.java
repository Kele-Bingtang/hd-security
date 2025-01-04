package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdBasicAuthHelper;

import java.util.function.Supplier;

/**
 * @author Tianke
 * @date 2025/1/3 22:59:11
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdBasicAuthHelperCreateSupplier extends Supplier<HdBasicAuthHelper> {
}
