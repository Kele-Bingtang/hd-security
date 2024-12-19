package cn.youngkbt.hdsecurity.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Hd Security 注解处理器接口
 *
 * @author Tianke
 * @date 2024/12/19 23:54:22
 * @since 1.0.0
 */
public interface HdAnnotationHandler<T extends Annotation> {

    /**
     * 获取所要处理的注解类型
     *
     * @return 注解类型
     */
    Class<T> getHandlerAnnotationClass();

    /**
     * 处理 Hd Security 注解，执行校验功能
     *
     * @param annotation 注解
     * @param method     注解所绑定的方法
     */
    void handle(T annotation, Method method);

    /**
     * 处理 Hd Security 注解，执行校验功能
     *
     * @param annotation 注解
     * @param method     注解所绑定的方法
     */
    default void handleAnnotation(Annotation annotation, Method method) {
        handle((T) annotation, method);
    }

}
