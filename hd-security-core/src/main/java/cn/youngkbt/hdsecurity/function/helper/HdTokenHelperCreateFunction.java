package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdTokenHelper;

import java.util.function.Function;

/**
 * 函数式接口：创建 HdTokenHelper 的策略
 * <pre>
 *     参数：账号类型 accountType
 *     返回：HdTokenHelper
 * </pre>
 *
 * @author Tianke
 * @date 2024/11/30 18:16:59
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdTokenHelperCreateFunction extends Function<String, HdTokenHelper> {
}
