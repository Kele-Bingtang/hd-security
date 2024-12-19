package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录认证校验
 *
 * @author Tianke
 * @date 2024/12/20 00:12:16
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckLogin {
    /**
     * 账号类型
     *
     * @return 账号类型
     */
    String accountType() default DefaultConstant.DEFAULT_ACCOUNT_TYPE;
}
