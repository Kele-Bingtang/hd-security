package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdAuthorizeHelper;

import java.util.function.Function;

/**
 * 函数式接口：创建 HdAuthorizeHelper 的策略
 * <pre>
 *     参数：账号类型 accountType
 *     返回：HdAuthorizeHelper
 * </pre>
 *
 * @author Tianke
 * @date 2024/12/17 22:45:42
 * @since 1.0.0
 */
public interface HdAuthorizeHelperCreateFunction extends Function<String, HdAuthorizeHelper> {
}
