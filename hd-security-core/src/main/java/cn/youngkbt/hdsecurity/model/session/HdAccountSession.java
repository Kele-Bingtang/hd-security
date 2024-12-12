package cn.youngkbt.hdsecurity.model.session;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

/**
 * @author Tianke
 * @date 2024/11/26 22:22:53
 * @since 1.0.0
 */
public class HdAccountSession extends HdSession {
    private String loginId;

    public HdAccountSession(String loginId) {
        this(loginId, DefaultConstant.DEFAULT_ACCOUNT_TYPE);
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
