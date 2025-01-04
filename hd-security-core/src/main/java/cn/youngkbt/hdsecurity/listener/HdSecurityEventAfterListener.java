package cn.youngkbt.hdsecurity.listener;

import cn.youngkbt.hdsecurity.annotation.handler.HdAnnotationHandler;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;

import java.lang.annotation.Annotation;

/**
 * Hd Security 后置事件监听器接口
 *
 * @author Tianke
 * @date 2024/11/25 21:54:11
 * @since 1.0.0
 */
public interface HdSecurityEventAfterListener {

    /**
     * 加载配置文件后触发
     */
    void afterLoadConfig(String accountType, HdSecurityConfig hdSecurityConfig);

    /**
     * 登录后触发
     *
     * @param accountType 账号类别
     * @param loginId     账号id
     * @param token       本次登录产生的 token 值
     * @param loginModel  登录参数
     */
    void afterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel);

    /**
     * 注销后触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param token       token 值
     */
    void afterLogout(String accountType, Object loginId, String token);

    /**
     * 踢人下线后触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param token       token 值
     */
    void afterKickout(String accountType, Object loginId, String token);

    /**
     * 顶人下线后触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param token       token 值
     */
    void afterReplaced(String accountType, Object loginId, String token);

    /**
     * 封禁账号后触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param disableTime 封禁时长，单位：秒
     * @param realm       封禁类型
     * @param level       封禁级别
     */
    void afterBanAccount(String accountType, Object loginId, long disableTime, String realm, int level);

    /**
     * 解封账号后触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param realm       解封原因
     */
    void afterUnBanAccount(String accountType, Object loginId, String realm);

    /**
     * 创建 Session 时触发
     *
     * @param sessionId SessionId
     */
    void afterCreateSession(String sessionId);

    /**
     * 注销 Session 时触发
     *
     * @param sessionId SessionId
     */
    void afterLogoutSession(String sessionId);

    /**
     * Token 续期时触发（注意：是 timeout 续期，而不是 active-timeout 续期）
     *
     * @param token      token 值
     * @param loginId    账号 ID
     * @param expireTime 续期时间
     */
    void afterRenewExpireTime(String token, Object loginId, long expireTime);

    /**
     * 二次认证开启后触发
     *
     * @param accountType    账号类别
     * @param token          webToken 值
     * @param realm          认证类型
     * @param secondAuthTime 认证时间
     */
    void afterSecondAuthOpen(String accountType, String token, String realm, long secondAuthTime);

    /**
     * 二次认证关闭后触发
     *
     * @param accountType 账号类别
     * @param token       webToken 值
     * @param realm       认证类型
     */
    void afterSecondAuthClose(String accountType, String token, String realm);

    /**
     * 全局组件注册后触发
     *
     * @param componentName   组件名称
     * @param componentObject 组件对象
     */
    void afterComponentRegister(String componentName, Object componentObject);

    /**
     * 注解处理器注册后触发
     *
     * @param annotationHandler 注解处理器
     */
    void afterRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler);
}
