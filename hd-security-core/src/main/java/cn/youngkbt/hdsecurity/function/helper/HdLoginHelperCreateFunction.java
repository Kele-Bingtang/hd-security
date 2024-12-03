package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdLoginHelper;

import java.util.function.Function;

/**
 * @author Tianke
 * @date 2024/11/30 18:15:32
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdLoginHelperCreateFunction extends Function<String, HdLoginHelper> {
}
