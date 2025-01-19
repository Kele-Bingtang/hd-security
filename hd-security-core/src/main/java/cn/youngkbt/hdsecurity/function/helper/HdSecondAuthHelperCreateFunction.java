package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdSecondAuthHelper;

import java.util.function.Function;

/**
 * 函数式接口：创建 HdSecondAuthHelper 的策略
 * <pre>
 *     参数：账号类型 accountType
 *     返回：HdSecondAuthHelper
 * </pre>
 *
 * @author Tianke
 * @date 2025/1/19 20:03:34
 * @since 1.0.0
 */
public interface HdSecondAuthHelperCreateFunction extends Function<String, HdSecondAuthHelper> {
}
