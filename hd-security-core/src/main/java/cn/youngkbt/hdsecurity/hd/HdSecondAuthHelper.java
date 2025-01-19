package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.exception.HdSecuritySecondAuthException;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

/**
 * @author Tianke
 * @date 2025/1/19 20:00:50
 * @since 1.0.0
 */
public class HdSecondAuthHelper {
    private final String accountType;

    public HdSecondAuthHelper() {
        this(DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdSecondAuthHelper(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    /**
     * 开启二级认证
     *
     * @param secondAuthTime 二级认证过期时间（单位: 秒）
     */
    public void openSecondAuth(long secondAuthTime) {
        openSecondAuth(DefaultConstant.DEFAULT_SECOND_AUTH_REALM, secondAuthTime);
    }

    /**
     * 开启指定领域的二级认证
     *
     * @param realm          领域
     * @param secondAuthTime 二级认证过期时间（单位: 秒）
     */
    public void openSecondAuth(String realm, long secondAuthTime) {
        // 登录后才能开启二级认证
        HdHelper.loginHelper(accountType).checkLogin();
        // 获取当前 Web Token，如果不存在，则抛出异常
        String webToken = HdHelper.tokenHelper(accountType).checkWebTokenNonNullThenGet();

        // 发布二级认证开启前置事件
        HdSecurityEventCenter.publishBeforeSecondAuthOpen(accountType, webToken, realm, secondAuthTime);
        // 添加二级认证表示到持久层
        HdSecurityManager.getRepository().add(getSecondAuthKey(webToken, realm), DefaultConstant.SECOND_AUTH_OPEN_TAG, secondAuthTime);

        // 发布二级认证开启后置事件
        HdSecurityEventCenter.publishAfterSecondAuthOpen(accountType, webToken, realm, secondAuthTime);
    }

    /**
     * 关闭二级认证
     */
    public void closeSecondAuth() {
        closeSecondAuth(DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 关闭指定领域的二级认证
     *
     * @param realm 领域
     */
    public void closeSecondAuth(String realm) {
        // 获取 Web 传来的 Token
        String webToken = HdHelper.tokenHelper(accountType).getWebToken();
        closeSecondAuth(webToken, realm);
    }

    /**
     * 关闭指定登录账号的指定领域二级认证
     *
     * @param token token
     * @param realm 领域
     */
    public void closeSecondAuth(String token, String realm) {
        if (HdStringUtil.hasEmpty(token)) {
            return;
        }
        // 发布二级认证关闭前置事件
        HdSecurityEventCenter.publishBeforeSecondAuthClose(accountType, token, realm);
        // 删除 Token 对应的二级认证标识
        HdSecurityManager.getRepository().remove(getSecondAuthKey(token, realm));
        // 发布二级认证关闭后置事件
        HdSecurityEventCenter.publishAfterSecondAuthClose(accountType, token, realm);
    }

    /**
     * 获取当前登录账号的二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     */
    public Long getSecondAuthTime() {
        return getSecondAuthTime(HdHelper.tokenHelper(accountType).getWebToken(), DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 获取当前登录账号指定领域的二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     */
    public Long getSecondAuthTime(String realm) {
        return getSecondAuthTime(HdHelper.tokenHelper(accountType).getWebToken(), realm);
    }

    /**
     * 获取指定登录账号指定领域的二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     *
     * @param token Token
     * @param realm 领域
     * @return 二级认证过期时间（单位: 秒, 返回 null 代表尚未通过二级认证）
     */
    public Long getSecondAuthTime(String token, String realm) {
        if (HdStringUtil.hasEmpty(token)) {
            return null;
        }

        // 登录后才能开启二级认证，因此这里获取当前 Token 对应的 loginId 来判断
        Object loginId = HdHelper.loginHelper(accountType).getLoginIdByToken(token);
        if (HdStringUtil.hasEmpty(loginId)) {
            return null;
        }

        // 从持久层获取二级认证的过期时间
        return HdSecurityManager.getRepository().getExpireTime(getSecondAuthKey(token, realm));
    }

    /**
     * 判断当前登录账号是否开启了二级认证
     */
    public boolean isSendAuth() {
        return isSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 判断当前登录账号指定领域的二级认证是否开启
     *
     * @param realm 领域
     */
    public boolean isSecondAuth(String realm) {
        return isSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), realm);
    }

    /**
     * 判断指定登录账号的指定领域是否开启了二级认证
     *
     * @param token Token
     * @return 是否开启了二级认证
     */
    public boolean isSecondAuth(String token, String realm) {
        if (HdStringUtil.hasEmpty(token)) {
            return false;
        }

        // 登录后才能开启二级认证，因此这里获取当前 Token 对应的 loginId 来判断
        Object loginId = HdHelper.loginHelper(accountType).getLoginIdByToken(token);
        if (HdStringUtil.hasEmpty(loginId)) {
            return false;
        }

        Object secondAuthTag = HdSecurityManager.getRepository().query(getSecondAuthKey(token, realm));
        return HdStringUtil.hasEmpty(secondAuthTag);
    }

    /**
     * 校验当前登录账号是否开启了二级认证
     */
    public void checkSecondAuth() {
        checkSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), DefaultConstant.DEFAULT_SECOND_AUTH_REALM);
    }

    /**
     * 校验当前登录账号指定领域的二级认证是否开启
     *
     * @param realm 领域
     */
    public void checkSecondAuth(String realm) {
        checkSecondAuth(HdHelper.tokenHelper(accountType).getWebToken(), realm);
    }

    /**
     * 校验指定登录账号的指定领域是否开启了二级认证
     *
     * @param token Token
     * @param realm 领域
     */
    public void checkSecondAuth(String token, String realm) {
        boolean secondAuth = isSecondAuth(token, realm);
        if (!secondAuth) {
            throw new HdSecuritySecondAuthException(accountType, token, realm);
        }
    }

    /**
     * 获取二级认证标识的 Key
     *
     * @param token Token
     * @param realm 领域
     * @return 二级认证标识的 Key
     */
    public String getSecondAuthKey(String token, String realm) {
        return RepositoryKeyHelper.getSecondAuthKey(accountType, token, realm);
    }
}
