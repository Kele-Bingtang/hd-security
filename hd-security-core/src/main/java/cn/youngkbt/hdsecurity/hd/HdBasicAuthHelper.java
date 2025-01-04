package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityHttpBasicAuthException;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * Hd Security Http Basic 认证模块
 *
 * @author Tianke
 * @date 2024/12/14 01:43:28
 * @since 1.0.0
 */
public class HdBasicAuthHelper {

    /**
     * 默认的 Realm 领域名称
     */
    public static final String DEFAULT_REALM = "Hd Security";

    public String getBasicAuthValue() {
        String authorization = HdSecurityManager.getContext().getRequest().getHeader("Authorization");

        if (HdStringUtil.hasEmpty(authorization) || !authorization.startsWith("Basic ")) {
            return null;
        }

        return Arrays.toString(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
    }

    public boolean isBasicAuth() {
        return isBasicAuth(HdSecurityManager.getConfig().getHttpBasicAccount());
    }

    public boolean isBasicAuth(String account) {
        if (HdStringUtil.hasEmpty(account)) {
            account = HdSecurityManager.getConfig().getHttpBasicAccount();
        }

        String basicAuthValue = getBasicAuthValue();
        return !HdStringUtil.hasEmpty(basicAuthValue) && Objects.equals(basicAuthValue, account);
    }

    public void checkBasicAuth() {
        checkBasicAuth(HdSecurityManager.getConfig().getHttpBasicAccount());
    }

    public void checkBasicAuth(String account) {
        checkBasicAuth(DEFAULT_REALM, account);
    }

    public void checkBasicAuth(String realm, String account) {
        boolean basicAuth = isBasicAuth(account);
        if (!basicAuth) {
            HdSecurityManager.getContext().getResponse()
                    .setStatus(401)
                    .addHeader("WWW-Authenticate", "Basic Realm=" + realm);

            throw new HdSecurityHttpBasicAuthException("Http Basic 认证失败：" + account).setCode(HdSecurityErrorCode.HTTP_BASIC_AUTH_FAIL);
        }
    }
}
