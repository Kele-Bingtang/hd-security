package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdLoginHelper;

import java.util.function.Function;

/**
 * 函数式接口：创建 HdLoginHelper 的策略
 * <pre>
 *     参数：账号类型 accountType
 *     返回：HdLoginHelper
 * </pre>
 *
 * @author Tianke
 * @date 2024/11/30 18:15:32
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdLoginHelperCreateFunction extends Function<String, HdLoginHelper> {
}
