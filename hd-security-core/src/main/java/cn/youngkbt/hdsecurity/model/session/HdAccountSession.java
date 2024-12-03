package cn.youngkbt.hdsecurity.model.session;

/**
 * @author Tianke
 * @date 2024/11/26 22:22:53
 * @since 1.0.0
 */
public class HdAccountSession extends HdSession {
    private String loginId;

    public HdAccountSession(String loginId) {
        this(loginId, null);
    }

    public HdAccountSession(String loginId, String accountType) {
        super(loginId, accountType);
        this.loginId = loginId;
        setType("account");
    }

    public String getLoginId() {
        return loginId;
    }

    public HdAccountSession setLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }
}
