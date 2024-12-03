package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdTokenHelper;

import java.util.function.Function;

/**
 * @author Tianke
 * @date 2024/11/30 18:16:59
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdTokenHelperCreateFunction extends Function<String, HdTokenHelper> {
}
