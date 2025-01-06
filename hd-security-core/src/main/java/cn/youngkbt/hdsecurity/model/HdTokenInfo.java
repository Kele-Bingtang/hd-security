package cn.youngkbt.hdsecurity.model;

/**
 * @author Tianke
 * @date 2025/1/7 00:20:34
 * @since 1.0.0
 */
public class HdTokenInfo {
    /** token 名称 */
    public String tokenName;

    /** token 值 */
    public String tokenValue;

    /** 此 token 是否已经登录 */
    public Boolean isLogin;

    /** 此 token 对应的 LoginId，未登录时为 null */
    public Object loginId;

    /** 多账号体系下的账号类型 */
    public String accountType;

    /** token 剩余有效期（单位: 秒） */
    public long tokenExpireTime;

    /** Account-Session 剩余有效时间（单位: 秒） */
    public long accountSessionExpireTime;

    /** Token-Session 剩余有效时间（单位: 秒） */
    public long tokenSessionExpireTime;

    /** token 距离被冻结还剩多少时间（单位: 秒） */
    public long tokenActiveExpireTime;

    /** 登录设备类型 */
    public String loginDevice;

    public String getTokenName() {
        return tokenName;
    }

    public HdTokenInfo setTokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public HdTokenInfo setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
        return this;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public HdTokenInfo setLogin(Boolean login) {
        isLogin = login;
        return this;
    }

    public Object getLoginId() {
        return loginId;
    }

    public HdTokenInfo setLoginId(Object loginId) {
        this.loginId = loginId;
        return this;
    }

    public String getAccountType() {
        return accountType;
    }

    public HdTokenInfo setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public HdTokenInfo setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
        return this;
    }

    public long getAccountSessionExpireTime() {
        return accountSessionExpireTime;
    }

    public HdTokenInfo setAccountSessionExpireTime(long accountSessionExpireTime) {
        this.accountSessionExpireTime = accountSessionExpireTime;
        return this;
    }

    public long getTokenSessionExpireTime() {
        return tokenSessionExpireTime;
    }

    public HdTokenInfo setTokenSessionExpireTime(long tokenSessionExpireTime) {
        this.tokenSessionExpireTime = tokenSessionExpireTime;
        return this;
    }

    public long getTokenActiveExpireTime() {
        return tokenActiveExpireTime;
    }

    public HdTokenInfo setTokenActiveExpireTime(long tokenActiveExpireTime) {
        this.tokenActiveExpireTime = tokenActiveExpireTime;
        return this;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public HdTokenInfo setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
        return this;
    }
}
