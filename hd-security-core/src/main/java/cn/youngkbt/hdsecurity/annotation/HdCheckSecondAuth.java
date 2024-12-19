package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 二级认证校验：客户端必须完成二级认证之后，才能进入该方法，否则将被抛出异常
 *
 * @author Tianke
 * @date 2024/12/20 00:38:11
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckSecondAuth {
    /**
     * 账号类型
     *
     * @return 账号类型
     */
    String accountType() default DefaultConstant.DEFAULT_ACCOUNT_TYPE;

    /**
     * 所在的领域
     *
     * @return 所在的领域
     */
    String realm() default DefaultConstant.DEFAULT_SECOND_AUTH_REALM;
}
