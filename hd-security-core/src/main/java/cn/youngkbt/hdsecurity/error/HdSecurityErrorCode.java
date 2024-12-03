package cn.youngkbt.hdsecurity.error;

/**
 * @author Tianke
 * @date 2024/11/25 22:29:42
 * @since 1.0.0
 */
public interface HdSecurityErrorCode {
    // --------- 全局组件异常码 ---------
    /**
     * 指定的配置文件加载失败
     */
    int CONFIG_LOAD_FAIL = 10001;
    /**
     * 配置文件属性无法正常读取
     */
    int CONFIG_PROPERTY_READ_FAIL = 10002;
    /**
     * 未能获取有效的上下文处理器
     */
    int CONTEXT_GET_NULL = 10003;
    /**
     * 注册的侦听器集合为空
     */
    int LISTENERS_IS_NULL = 10011;
    /**
     * 注册的侦听器集合为空
     */
    int LISTENER_IS_NULL = 10012;

    /**
     * 登录时的账号 id 值为空
     */
    int LOGIN_ID_IS_NULL = 10101;
    /**
     * 未能读取到有效 Token
     */
    int TOKEN_IS_NULL = 10102;
    /**
     * 前端未按照指定的前缀提交 token
     */
    int TOKEN_NO_MATCH_PREFIX = 10103;
    /**
     * 获取 Session 时提供的 SessionId 为空
     */
    int SESSION_ID_IS_NULL = 10201;
}
