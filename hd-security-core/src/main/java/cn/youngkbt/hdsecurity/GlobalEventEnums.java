package cn.youngkbt.hdsecurity;

/**
 * 全局事件枚举类
 *
 * @author Tianke
 * @date 2024/11/26 00:51:51
 * @since 1.0.0
 */
public enum GlobalEventEnums {

    LOAD_CONFIG("loadConfig", "加载配置"),
    LOGIN("login", "登录"),
    LOGOUT("logout", "注销"),
    KICKOUT("kickout", "踢人下线"),
    REPLACED("replaced", "顶人下线"),
    BAN("ban", "账号封禁"),
    UN_BAN("unBan", "账号解封"),
    CREATE_SESSION("CreateSession", "创建 Session"),
    LOGOUT_SESSION("LogoutSession", "注销 Session"),
    RENEW_EXPIRE_TIME("RenewExpireTime", "刷新 Session 过期时间"),
    SECOND_AUTH_OPEN("secondAuthOpen", "开启二次认证"),
    SECOND_AUTH_CLOSE("secondAuthClose", "关闭二次认证"),
    REGISTER_LOG("HdSecurityLog", "加载日志组件"),
    REGISTER_REPOSITORY("HdSecurityRepository", "加载持久仓库组件"),
    REGISTER_CONTEXT("HdSecurityContext", "加载上下文组件"),

    ;

    private final String functionName;
    private final String functionDesc;

    GlobalEventEnums(String functionName, String functionDesc) {
        this.functionName = functionName;
        this.functionDesc = functionDesc;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionDesc() {
        return functionDesc;
    }

    public static GlobalEventEnums getByFunctionName(String functionName) {
        for (GlobalEventEnums value : values()) {
            if (functionName.equals(value.getFunctionName())) {
                return value;
            }
        }
        return null;
    }

}
