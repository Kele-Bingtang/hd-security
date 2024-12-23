package cn.youngkbt.hdsecurity.annotation.handler;

import cn.youngkbt.hdsecurity.annotation.HdCheckOr;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.hdsecurity.hd.HdHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * HdCheckOr 处理器，执行多个鉴权注解的校验功能（任意鉴权注解通过后就通过）
 *
 * @author Tianke
 * @date 2024/12/20 00:59:02
 * @since 1.0.0
 */
public class HdCheckOrHandler implements HdAnnotationHandler<HdCheckOr> {
    @Override
    public Class<HdCheckOr> getHandlerAnnotationClass() {
        return HdCheckOr.class;
    }

    @Override
    public void handle(HdCheckOr annotation, Method method) {
        List<Annotation> annotationList = new ArrayList<>();
        annotationList.addAll(List.of(annotation.login()));
        annotationList.addAll(List.of(annotation.permission()));
        annotationList.addAll(List.of(annotation.role()));
        annotationList.addAll(List.of(annotation.secondAuth()));
        annotationList.addAll(List.of(annotation.httpBasic()));

        // 如果 annotationList 为空，说明不存在任何鉴权注解
        if (annotationList.isEmpty()) {
            return;
        }

        List<HdSecurityException> errorList = new ArrayList<>();
        for (Annotation a : annotationList) {
            try {
                HdHelper.annotationHelper().getAnnotationHandler(a.annotationType()).handleAnnotation(a, method);
                // 只要有一个校验通过，就直接返回，否则捕获异常继续校验
                return;
            } catch (HdSecurityException e) {
                errorList.add(e);
            }
        }

        throw new HdSecurityException(errorList.get(0));
    }
}
