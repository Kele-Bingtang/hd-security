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

    /**
     * 获取 Http Basic 认证的账号
     *
     * @return 账号
     */
    public String getBasicAuthValue() {
        String authorization = HdSecurityManager.getContext().getRequest().getHeader("Authorization");

        if (HdStringUtil.hasEmpty(authorization) || !authorization.startsWith("Basic ")) {
            return null;
        }

        return Arrays.toString(Base64.getDecoder().decode(authorization.substring("Basic ".length())));
    }

    /**
     * Http Basic 认证校验是否成功
     *
     * @return Http Basic 认证校验是否成功
     */
    public boolean isBasicAuth() {
        return isBasicAuth(HdSecurityManager.getConfig().getHttpBasicAccount());
    }

    /**
     * Http Basic 认证校验账号是否成功
     *
     * @param account 账号
     * @return Http Basic 认证校验账号是否成功
     */
    public boolean isBasicAuth(String account) {
        if (HdStringUtil.hasEmpty(account)) {
            account = HdSecurityManager.getConfig().getHttpBasicAccount();
        }

        String basicAuthValue = getBasicAuthValue();
        return !HdStringUtil.hasEmpty(basicAuthValue) && Objects.equals(basicAuthValue, account);
    }

    /**
     * Http Basic 认证校验是否成功，校验失败则抛出 HdSecurityHttpBasicAuthException 异常
     *
     * @throws HdSecurityHttpBasicAuthException Http Basic 认证失败
     */
    public void checkBasicAuth() {
        checkBasicAuth(HdSecurityManager.getConfig().getHttpBasicAccount());
    }

    /**
     * Http Basic 认证带有账号的校验是否成功，校验失败则抛出 HdSecurityHttpBasicAuthException 异常
     *
     * @param account 账号
     * @throws HdSecurityHttpBasicAuthException Http Basic 认证失败
     */
    public void checkBasicAuth(String account) {
        checkBasicAuth(DEFAULT_REALM, account);
    }

    /**
     * Http Basic 认证带有领域的校验是否成功，校验失败则抛出 HdSecurityHttpBasicAuthException 异常
     *
     * @param realm   领域名称
     * @param account 账号
     * @throws HdSecurityHttpBasicAuthException Http Basic 认证失败
     */
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
