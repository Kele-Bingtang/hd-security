package cn.youngkbt.hdsecurity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 批量注解鉴权
 *
 * @author Tianke
 * @date 2024/12/20 00:58:35
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HdCheckOr {
    /**
     * 登录鉴权注解，参考 {@link HdCheckLogin}
     *
     * @return HdCheckLogin 数组
     */
    HdCheckLogin[] login() default {};

    /**
     * 权限鉴权注解，参考 {@link HdCheckPermission}
     *
     * @return HdCheckPermission 数组
     */
    HdCheckPermission[] permission() default {};

    /**
     * 角色鉴权注解，参考 {@link HdCheckRole}
     *
     * @return HdCheckRole 数组
     */
    HdCheckRole[] role() default {};

    /**
     * 二次验证鉴权注解，参考 {@link HdCheckSecondAuth}
     *
     * @return HdCheckSecondAuth 数组
     */
    HdCheckSecondAuth[] secondAuth() default {};

    /**
     * http Basic 鉴权注解，参考 {@link HdCheckHttpBasic}
     *
     * @return
     */
    HdCheckHttpBasic[] httpBasic() default {};

}
