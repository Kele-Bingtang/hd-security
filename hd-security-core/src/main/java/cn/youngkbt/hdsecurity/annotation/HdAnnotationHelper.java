package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.annotation.handler.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Hd Security 注解处理器
 *
 * @author Tianke
 * @date 2024/12/19 23:52:41
 * @since 1.0.0
 */
public class HdAnnotationHelper {

    private Map<Class<? extends Annotation>, HdAnnotationHandler<? extends Annotation>> annotationHandlerMap = new LinkedHashMap<>();

    public HdAnnotationHelper() {
        annotationHandlerMap.put(HdIgnore.class, new HdIgnoreHandler());
        annotationHandlerMap.put(HdCheckLogin.class, new HdCheckLoginHandler());
        annotationHandlerMap.put(HdCheckRole.class, new HdCheckRoleHandler());
        annotationHandlerMap.put(HdCheckPermission.class, new HdCheckPermissionHandler());
        annotationHandlerMap.put(HdCheckSecondAuth.class, new HdCheckSecondAuthHandler());
        annotationHandlerMap.put(HdCheckHttpBasic.class, new HdCheckHttpBasicHandler());
        annotationHandlerMap.put(HdCheckOr.class, new HdCheckOrHandler());
    }

    /**
     * 获取注解处理器集合
     *
     * @return 注解处理器集合
     */
    public Map<Class<? extends Annotation>, HdAnnotationHandler<? extends Annotation>> getAnnotationHandlerMap() {
        return annotationHandlerMap;
    }

    /**
     * 设置注解处理器集合
     *
     * @param annotationHandlerMap 注解处理器集合
     */
    public void setAnnotationHandlerMap(Map<Class<? extends Annotation>, HdAnnotationHandler<? extends Annotation>> annotationHandlerMap) {
        this.annotationHandlerMap = annotationHandlerMap;
    }

    /**
     * 获取指定注解处理器
     *
     * @param annotationClass 注解类
     * @return 注解处理器
     */
    public HdAnnotationHandler<? extends Annotation> getAnnotationHandler(Class<? extends Annotation> annotationClass) {
        return annotationHandlerMap.get(annotationClass);
    }

    /**
     * 添加注解处理器
     *
     * @param annotationHandler 注解处理器
     */
    public void addAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
        addAnnotationHandler(annotationHandler.getHandlerAnnotationClass(), annotationHandler);
    }

    /**
     * 添加注解处理器
     *
     * @param annotationClass   注解类
     * @param annotationHandler 注解处理器
     */
    public void addAnnotationHandler(Class<? extends Annotation> annotationClass, HdAnnotationHandler<? extends Annotation> annotationHandler) {
        this.annotationHandlerMap.put(annotationClass, annotationHandler);
    }

    /**
     * 移除注解处理器
     *
     * @param annotationClass 注解类
     */
    public void removeAnnotationHandler(Class<? extends Annotation> annotationClass) {
        annotationHandlerMap.remove(annotationClass);
    }

    /**
     * 清空注解处理器
     */
    public void clearAnnotationHandler() {
        annotationHandlerMap.clear();
    }

    /**
     * 注解处理器校验。对方法上或者方法所在类的 Hd Security 注解进行校验
     */
    public Consumer<Method> handle = method -> annotationHandlerMap.forEach((key, value) -> {

        // 再从方法所在类上获取注解判断
        Annotation classHdAnnotation = method.getDeclaringClass().getAnnotation(key);
        if (null != classHdAnnotation) {
            value.handleAnnotation(classHdAnnotation, method);
        }

        // 先从方法上获取注解判断
        Annotation methodHdAnnotation = method.getAnnotation(key);
        if (null != methodHdAnnotation) {
            value.handleAnnotation(methodHdAnnotation, method);
        }
    });

}
