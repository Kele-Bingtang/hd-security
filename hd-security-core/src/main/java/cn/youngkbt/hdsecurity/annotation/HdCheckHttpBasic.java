package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.hd.HdBasicAuthHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Http Basic 认证校验
 *
 * @author Tianke
 * @date 2024/12/20 00:49:13
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckHttpBasic {
    /**
     * 领域
     *
     * @return 领域
     */
    String realm() default HdBasicAuthHelper.DEFAULT_REALM;

    /**
     * 需要校验的账号密码，格式形如 hd:123456
     *
     * @return 账号密码
     */
    String account() default "";
}
