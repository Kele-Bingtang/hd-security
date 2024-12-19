package cn.youngkbt.hdsecurity.annotation;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证校验
 *
 * @author Tianke
 * @date 2024/12/20 00:46:44
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckPermission {
    /**
     * 账号类型
     *
     * @return 账号类型
     */
    String accountType() default DefaultConstant.DEFAULT_ACCOUNT_TYPE;

    /**
     * 权限码
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

    /**
     * 在权限校验不通过时校验角色，两者只要其一校验成功即可通过校验
     *
     * @return 角色码
     */
    String[] orRole() default {};
}
