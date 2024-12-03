package cn.youngkbt.hdsecurity.function;

import cn.youngkbt.hdsecurity.exception.HdSecurityException;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 函数式接口：生成唯一元素的函数式接口
 *
 * @author TianKe
 * @date 2024-11-28 01:18:31
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdGenerateUniqueElementFunction {

    /**
     * 封装 元素 生成、校验的代码，生成 唯一元素
     *
     * @param elementName           要生成的元素名称，如 Token，Code 码，ID 等唯一元素
     * @param maxTryTimes           最大尝试次数
     * @param createElementFunction 创建 唯一元素 的函数
     * @param checkUniquePredicate  校验 元素 是否唯一的函数（返回 true 表示唯一，可用）
     * @return 最终生成的唯一元素
     */
    String generate(
            String elementName,
            int maxTryTimes,
            Supplier<String> createElementFunction,
            Predicate<String> checkUniquePredicate,
            Consumer<HdSecurityException> exceptionConsumer
    );

}