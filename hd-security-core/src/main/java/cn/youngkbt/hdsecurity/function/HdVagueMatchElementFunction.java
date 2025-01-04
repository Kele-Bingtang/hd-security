package cn.youngkbt.hdsecurity.function;

import java.util.List;
import java.util.function.BiFunction;

/**
 * 函数式接口：判断集合中是否包含指定元素（模糊匹配，支持 * 匹配）策略
 * <pre>
 * 参数 1：字符串集合
 * 参数 2：要匹配的字符串
 * 返回值：是否包含匹配的字符串
 * </pre>
 *
 * @author Tianke
 * @date 2024/11/30 18:33:43
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdVagueMatchElementFunction extends BiFunction<List<String>, String, Boolean> {
}
