package cn.youngkbt.hdsecurity.function.helper;

import cn.youngkbt.hdsecurity.hd.HdTempTokenHelper;

import java.util.function.Supplier;

/**
 * 函数式接口：创建 HdTempTokenHelper 的策略
 * <pre>
 *     返回：HdTempTokenHelper
 * </pre>
 *
 * @author Tianke
 * @date 2025/1/5 02:49:56
 * @since 1.0.0
 */
public interface HdTempTokenHelperCreateFunction extends Supplier<HdTempTokenHelper> {
}
