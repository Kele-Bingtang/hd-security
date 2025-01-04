package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdBanAccountHelper;

import java.util.function.Function;

/**
 * 函数式接口：创建 HdBanAccountHelper 的策略
 * <pre>
 *     参数：账号类型 accountType
 *     返回：HdBanAccountHelper
 * </pre>
 *
 * @author Tianke
 * @date 2024/12/17 22:46:09
 * @since 1.0.0
 */
public interface HdBanAccountHelperCreateFunction extends Function<String, HdBanAccountHelper> {
}
