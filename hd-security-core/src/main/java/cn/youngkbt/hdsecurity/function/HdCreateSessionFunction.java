package cn.youngkbt.hdsecurity.function;

import cn.youngkbt.hdsecurity.model.session.HdSession;

import java.util.function.BiFunction;

/**
 * 函数式接口：创建 HdSession 的策略
 * <pre>
 *     参数：Session ID
 *     返回：HdSession 对象
 * </pre>
 *
 * @author Tianke
 * @date 2024/11/30 18:10:13
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdCreateSessionFunction<S extends HdSession> extends BiFunction<String, String, S> {
}
