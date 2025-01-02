package cn.youngkbt.hdsecurity.aop;

import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import cn.youngkbt.hdsecurity.hd.HdAnnotationHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Hd Security 注解方法拦截器
 *
 * @author Tianke
 * @date 2025/1/2 23:21:37
 * @since 1.0.0
 */
public class HdSecurityAopAnnotationMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Method method = invocation.getMethod();
            HdAnnotationHelper hdAnnotationHelper = HdHelper.annotationHelper();
            hdAnnotationHelper.handle.accept(method);
        } catch (HdSecurityStopException e) {
            // HdSecurityStopException 异常代表：停止匹配，进入 Controller

        }
        return invocation.proceed();
    }
}