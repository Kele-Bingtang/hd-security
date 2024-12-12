package cn.youngkbt.hdsecurity.model.session;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

/**
 * @author Tianke
 * @date 2024/11/26 22:23:03
 * @since 1.0.0
 */
public class HdTokenSession extends HdSession {
    private String token;

    public HdTokenSession(String token) {
        this(token, DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdTokenSession(String token, String accountType) {
        super(token, accountType);
        this.token = token;
        setToken("token");
    }

    public String getToken() {
        return token;
    }

    public HdTokenSession setToken(String token) {
        this.token = token;
        return this;
    }
}
