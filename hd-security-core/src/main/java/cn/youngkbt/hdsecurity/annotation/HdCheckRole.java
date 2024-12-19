package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色认证校验
 *
 * @author Tianke
 * @date 2024/12/20 00:42:05
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckRole {
    /**
     * 账号类型
     *
     * @return 账号类型
     */
    String accountType() default DefaultConstant.DEFAULT_ACCOUNT_TYPE;

    /**
     * 角色码
     *
     * @return 角色码
     */
    String[] value() default {};

    /**
     * 校验模式：AND、OR，默认 AND
     *
     * @return 校验模式
     */
    HdMode mode() default HdMode.AND;
    
    String[] roles() default {};

    /**
     * 在角色校验不通过时校验权限，两者只要其一校验成功即可通过校验
     *
     * @return 校验码
     */
    String[] orPermission() default {};
}
