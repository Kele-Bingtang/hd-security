package cn.youngkbt.hdsecurity.aop.config;

import cn.youngkbt.hdsecurity.annotation.handler.HdAnnotationHandler;
import cn.youngkbt.hdsecurity.aop.HdSecurityAopAnnotationMethodInterceptor;
import cn.youngkbt.hdsecurity.aop.helper.HdSecurityAopDynamicProxyHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Hd Security AOP 环绕切入 Bean 注册
 *
 * @author Tianke
 * @date 2025/1/2 22:54:24
 * @since 1.0.0
 */
@AutoConfiguration(after = HdAnnotationHandler.class)
public class HdSecurityAopAnnotationAutoConfiguration {

    @Bean
    public HdSecurityAopDynamicProxyHelper hdSecurityAopDynamicProxyHelper() {
        return new HdSecurityAopDynamicProxyHelper();
    }

    @Bean
    public DefaultPointcutAdvisor hdSecurityAopAnnotationAdvisor(List<HdAnnotationHandler<? extends Annotation>> annotationHandlerList) {
        String packagePath = registerHdSecurityAnnotationAndGetExpression(annotationHandlerList);

        HdSecurityAopDynamicProxyHelper proxyHelper = hdSecurityAopDynamicProxyHelper();

        return proxyHelper.getAdvisor(packagePath, new HdSecurityAopAnnotationMethodInterceptor());
    }

    public String registerHdSecurityAnnotationAndGetExpression(List<HdAnnotationHandler<? extends Annotation>> annotationHandlerList) {
        List<Class<? extends Annotation>> classList = new ArrayList<>(HdHelper.annotationHelper().getAnnotationHandlerMap().keySet());

        if (HdCollectionUtil.isNotEmpty(annotationHandlerList)) {
            for (HdAnnotationHandler<? extends Annotation> handler : annotationHandlerList) {
                Class<? extends Annotation> annotationClass = handler.getHandlerAnnotationClass();
                if (!classList.contains(annotationClass)) {
                    classList.add(annotationClass);
                }
            }
        }

        return getHdSecurityAnnotationExpression(classList);
    }

    public String getHdSecurityAnnotationExpression(List<Class<? extends Annotation>> classList) {
        StringBuilder pointcutExpression = new StringBuilder();
        for (Class<?> cls : classList) {
            if (!pointcutExpression.isEmpty()) {
                pointcutExpression.append(" || ");
            }
            // @annotation 拦截方法级别，@within 拦截对象级别
            pointcutExpression.append("@within(")
                    .append(cls.getName())
                    .append(") || @annotation(")
                    .append(cls.getName())
                    .append(")");
        }

        return pointcutExpression.toString();
    }
}
