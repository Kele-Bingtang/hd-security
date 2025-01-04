package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 二次认证异常，检查二次认证不合法时抛出
 *
 * @author Tianke
 * @date 2024/12/14 01:17:52
 * @since 1.0.0
 */
public class HdSecuritySecondAuthException extends HdSecurityException {

    /**
     * 账号类型
     */
    private String accountType;

    /**
     * 未通过校验的 Token
     */
    private Object token;

    /**
     * 未通过校验的领域
     */
    private String realm;

    public String getAccountType() {
        return accountType;
    }

    public Object getToken() {
        return token;
    }

    public String getRealm() {
        return realm;
    }

    public HdSecuritySecondAuthException(String accountType, String token, String realm) {
        super(token + " 二次认证校验失败：" + realm);
        this.token = token;
        this.accountType = accountType;
        this.realm = realm;
    }
}
