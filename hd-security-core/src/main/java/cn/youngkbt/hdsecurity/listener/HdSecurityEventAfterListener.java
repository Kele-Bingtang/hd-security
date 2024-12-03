package cn.youngkbt.hdsecurity.listener;

import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;

/**
 * @author Tianke
 * @date 2024/11/25 21:54:11
 * @since 1.0.0
 */
public interface HdSecurityEventAfterListener {

    /**
     * 加载配置文件后触发
     */
    void afterLoadConfig(HdSecurityConfig hdSecurityConfig);

    /**
     * 登录后触发
     *
     * @param accountType  账号类别
     * @param loginId    账号id
     * @param token      本次登录产生的 token 值
     * @param loginModel 登录参数
     */
    void afterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel);

    /**
     * 注销后触发
     *
     * @param accountType 账号类别
     * @param loginId   账号 ID
     * @param token     token 值
     */
    void afterLogout(String accountType, Object loginId, String token);

    /**
     * 踢人下线后触发
     *
     * @param accountType 账号类别
     * @param loginId   账号 ID
     * @param token     token 值
     */
    void afterKickout(String accountType, Object loginId, String token);

    /**
     * 顶人下线后触发
     *
     * @param accountType 账号类别
     * @param loginId   账号 ID
     * @param token     token 值
     */
    void afterReplaced(String accountType, Object loginId, String token);

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
     * @param token   token 值
     * @param loginId 账号 ID
     * @param expireTime 续期时间
     */
    void afterRenewExpireTime(String token, Object loginId, long expireTime);

    /**
     * 全局组件注册后触发
     *
     * @param componentName   组件名称
     * @param componentObject 组件对象
     */
    void afterComponentRegister(String componentName, Object componentObject);
}
