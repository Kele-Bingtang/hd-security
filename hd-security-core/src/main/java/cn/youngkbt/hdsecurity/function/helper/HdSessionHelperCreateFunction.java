package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdSessionHelper;

import java.util.function.Function;

/**
 * 函数式接口：创建 HdSessionHelper 的策略
 * <pre>
 *     参数：账号类型 accountType
 *     返回：HdSessionHelper
 * </pre>
 *
 * @author Tianke
 * @date 2024/11/30 18:16:21
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSessionHelperCreateFunction extends Function<String, HdSessionHelper> {
}
