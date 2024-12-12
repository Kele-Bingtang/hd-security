package cn.youngkbt.hdsecurity.exception;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;

/**
 * @author Tianke
 * @date 2024/12/12 23:35:09
 * @since 1.0.0
 */
public class HdSecurityAuthorizeException extends HdSecurityException {
    /**
     * 认证信息
     */
    private final String authorize;
    /**
     * 认证类型
     */
    private final String authorizeType;
    /**
     * 账号类型
     */
    private final String accountType;

    public HdSecurityAuthorizeException(String authorities, String authorizeType) {
        this(authorities, authorizeType, DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdSecurityAuthorizeException(String authorize, String authorizeType, String accountType) {
        super(authorizeType + " 认证失败，无此认证信息：" + authorize);
        this.authorize = authorize;
        this.authorizeType = authorizeType;
        this.accountType = accountType;
    }

    public String getAuthorize() {
        return authorize;
    }

    public String getAuthorizeType() {
        return authorizeType;
    }

    public String getAccountType() {
        return accountType;
    }
}
