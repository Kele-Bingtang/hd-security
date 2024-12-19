package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 账号禁用校验
 *
 * @author Tianke
 * @date 2024/12/20 00:53:36
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckDisable {

    /**
     * 账号类型
     *
     * @return 账号类型
     */
    String accountType() default DefaultConstant.DEFAULT_ACCOUNT_TYPE;

    /**
     * 领域标识
     *
     * @return 领域标识
     */
    String[] value() default {DefaultConstant.DEFAULT_BAN_REALM};

    /**
     * 禁用级别
     *
     * @return 禁用级别
     */
    int level() default DefaultConstant.DEFAULT_BAN_LEVEL;
}
