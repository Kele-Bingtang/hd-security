package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdSessionHelper;

import java.util.function.Function;

/**
 * @author Tianke
 * @date 2024/11/30 18:16:21
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSessionHelperCreateFunction extends Function<String, HdSessionHelper> {
}
