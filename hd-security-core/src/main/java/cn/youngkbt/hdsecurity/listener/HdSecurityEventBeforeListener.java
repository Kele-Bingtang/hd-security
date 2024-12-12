package cn.youngkbt.hdsecurity.listener;

/**
 * Hd Security 事件监听器
 *
 * @author Tianke
 * @date 2024/11/25 21:37:18
 * @since 1.0.0
 */
public interface HdSecurityEventBeforeListener {

    /**
     * 加载配置前触发
     */
    void beforeLoadConfig();

    /**
     * 登录前触发
     */
    void beforeLogin(String accountType, Object loginId);

    /**
     * 注销前触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     */
    void beforeLogout(String accountType, Object loginId);

    /**
     * 踢人下线前触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     */
    void beforeKickout(String accountType, Object loginId);

    /**
     * 顶人下线前触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     */
    void beforeReplaced(String accountType, Object loginId);

    /**
     * 封禁账号前触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param disableTime        封禁时长，单位：秒
     * @param realm       封禁原因
     * @param level       封禁级别
     */
    void beforeBanAccount(String accountType, Object loginId, long disableTime, String realm, int level);

    /**
     * 解封账号前触发
     *
     * @param accountType 账号类别
     * @param loginId     账号 ID
     * @param realm       解封原因
     */
    void beforeUnBanAccount(String accountType, Object loginId, String realm);
    
    /**
     * 创建 Session 前触发
     *
     * @param sessionId SessionId
     */
    void beforeCreateSession(String sessionId);

    /**
     * 注销 Session 前触发
     *
     * @param sessionId SessionId
     */
    void beforeLogoutSession(String sessionId);

    /**
     * Token 续期时触发（注意：是 timeout 续期，而不是 active-timeout 续期）
     *
     * @param token      token 值
     * @param loginId    账号 ID
     * @param expireTime 续期时间
     */
    void beforeRenewExpireTime(String token, Object loginId, long expireTime);

    /**
     * 全局组件注册前触发
     *
     * @param componentName   组件名称
     * @param componentObject 组件对象
     */
    void beforeComponentRegister(String componentName, Object componentObject);

}
