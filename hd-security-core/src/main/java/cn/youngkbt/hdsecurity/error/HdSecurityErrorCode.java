package cn.youngkbt.hdsecurity.error;

/**
 * Hd Security 异常错误码
 *
 * @author Tianke
 * @date 2024/11/25 22:29:42
 * @since 1.0.0
 */
public interface HdSecurityErrorCode {
    // --------- 全局组件异常码 ---------
    /**
     * 配置文件属性无法正常读取
     */
    int CONFIG_PROPERTY_READ_FAIL = 10001;
    /**
     * 未能获取有效的上下文处理器
     */
    int CONTEXT_GET_NULL = 10002;
    /**
     * 注册的侦听器集合为空
     */
    int LISTENERS_IS_NULL = 10003;
    /**
     * 注册的侦听器集合为空
     */
    int LISTENER_IS_NULL = 10004;

    /**
     * 登录时的账号 id 值为空
     */
    int LOGIN_ID_IS_NULL = 10101;
    /**
     * 未能读取到有效 Token
     */
    int TOKEN_IS_NULL = 10102;
    /**
     * Token 无效（Token 对应的账号不存在）
     */
    int TOKEN_INVALID = 10103;
    /**
     * 前端未按照指定的前缀提交 token
     */
    int TOKEN_NO_MATCH_PREFIX = 10104;
    /**
     * Token 已被冻结
     */
    int TOKEN_FREEZE = 10105;
    /**
     * Token 已被踢下线
     */
    int TOKEN_KICK_OUT = 10106;
    /**
     * Token 已被顶下线
     */
    int TOKEN_REPLACED = 10107;
    /**
     * LoginId 为 Hd Security 关键词
     */
    int LOGIN_ID_IS_KEYWORD = 10108;

    /**
     * 获取 Session 时提供的 SessionId 为空
     */
    int SESSION_ID_IS_NULL = 10201;
    /**
     * 获取 Token-Session 时提供的 token 为空
     */
    int TOKEN_IS_NULL_WHEN_GET_TOKEN_SESSION = 10211;

    /**
     * 封禁的账号无效
     */
    int BAN_ACCOUNT_INVALID = 10301;

    /**
     * 封禁的领域无效
     */
    int BAN_REALM_INVALID = 10302;

    /**
     * 封禁的级别无效
     */
    int BAN_LEVEL_INVALID = 10303;
    /**
     * 当前账号未通过领域封禁校验
     */
    int BAN_NOT_PASS = 10304;

    /**
     * 认证的角色码无效
     */
    int AUTHORIZE_ROLE_INVALID = 10401;
    /**
     * 认证的权限码无效
     */
    int AUTHORIZE_PERMISSION_INVALID = 10402;

    /**
     * 未能通过 Http Basic 认证校验
     */
    int HTTP_BASIC_AUTH_FAIL = 10501;

    /**
     * 无效的 SameOrigin Token
     */
    int SAME_ORIGIN_TOKEN_INVALID = 10502;

}
