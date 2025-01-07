package cn.youngkbt.hdsecurity.aop.helper;

import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.InvocationTargetException;

/**
 * Hd Security AOP 插拔式 动态实现 AOP 模块
 *
 * <p>
 * 文档：
 * 1、https://www.jb51.net/program/297714rev.htm
 * 2、https://blog.csdn.net/Tomwildboar/article/details/139199801
 * </p>
 *
 * @author Tianke
 * @date 2025/1/2 23:35:00
 * @since 1.0.0
 */
public class HdSecurityAopDynamicProxyHelper {

    /**
     * 获取 Advisor（比如拦截器 MethodInterceptor）
     *
     * @param expression AOP 表达式，如 @annotation(xxx)
     * @param advice     自定义 Advice 实现类
     * @return Advisor
     */
    public DefaultPointcutAdvisor getAdvisor(String expression, Advice advice) {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        // 设置切点
        defaultPointcutAdvisor.setPointcut(aspectJExpressionPointcut);
        // 设置 Advice 实现类，如拦截器
        defaultPointcutAdvisor.setAdvice(advice);

        return defaultPointcutAdvisor;
    }

    /**
     * 获取 Advisor（比如拦截器 MethodInterceptor）
     *
     * @param expression        AOP 表达式，如 @annotation(xxx)
     * @param advicePackagePath 自定义 Advice 类路径
     * @return Advisor
     */
    public DefaultPointcutAdvisor getAdvisor(String expression, String advicePackagePath) {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        // 设置切点
        defaultPointcutAdvisor.setPointcut(aspectJExpressionPointcut);

        try {
            // 获取 Advice 实现类
            Object instance = Class.forName(advicePackagePath).getDeclaredConstructor().newInstance();
            if (instance instanceof Advice advice) {
                // 设置 Advice 实现类，如拦截器
                defaultPointcutAdvisor.setAdvice(advice);
            } else {
                throw new HdSecurityException("提供的 Advice 类路径加载不是 Advice 子类");
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new HdSecurityException("提供的 Advice 类路径无法找到 Advice 类" + e.getMessage());
        }

        return defaultPointcutAdvisor;
    }
}
