package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.session.HdAccountSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenSession;
import cn.youngkbt.hdsecurity.strategy.HdSecurityHelperCreateStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hd Security 模块门户
 *
 * @author Tianke
 * @date 2024/11/30 16:07:12
 * @since 1.0.0
 */
public class HdHelper {

    private HdHelper() {
    }

    /**
     * Key 为 accountType，value 为 helper 类
     */
    private static final Map<String, HdLoginHelper> LOGIN_HELPER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, HdSessionHelper> SESSION_HELPER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, HdTokenHelper> TOKEN_HELPER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, HdAuthorizeHelper> AUTHORIZE_HELPER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, HdBanAccountHelper> BAN_ACCOUNT_HELPER_MAP = new ConcurrentHashMap<>();
    private static HdAnnotationHelper hdAnnotationHelper;
    private static HdBasicAuthHelper hdBasicAuthHelper;
    private static HdSameOriginTokenHelper hdSameOriginTokenHelper;
    /**
     * 默认的 accountType
     */
    private static final String ACCOUNT_TYPE = DefaultConstant.DEFAULT_ACCOUNT_TYPE;

    /**
     * 获取 HdSessionHelper，如果获取不到则根据 accountType 创建
     *
     * @return HdSessionHelper
     */
    public static HdLoginHelper loginHelper() {
        return loginHelper(ACCOUNT_TYPE);
    }

    /**
     * 根据 accountType 获取 HdSessionHelper，如果获取不到则根据 accountType 创建
     *
     * @param accountType 账号类型
     * @return HdSessionHelper
     */
    public static HdLoginHelper loginHelper(String accountType) {
        return LOGIN_HELPER_MAP.computeIfAbsent(accountType, key -> HdSecurityHelperCreateStrategy.instance.getCreateLoginHelper().apply(key));
    }

    /**
     * 获取 HdSessionHelper，如果获取不到则根据 accountType 创建
     *
     * @return HdSessionHelper
     */
    public static HdSessionHelper sessionHelper() {
        return sessionHelper(ACCOUNT_TYPE);
    }

    /**
     * 根据 accountType 获取 HdSessionHelper，如果获取不到则根据 accountType 创建
     *
     * @param accountType 账号类型
     * @return HdSessionHelper
     */
    public static HdSessionHelper sessionHelper(String accountType) {
        return SESSION_HELPER_MAP.computeIfAbsent(accountType, key -> HdSecurityHelperCreateStrategy.instance.getCreateSessionHelper().apply(key));
    }

    /**
     * 获取 HdTokenHelper，如果获取不到则根据 accountType 创建
     *
     * @return HdTokenHelper
     */
    public static HdTokenHelper tokenHelper() {
        return tokenHelper(ACCOUNT_TYPE);
    }

    /**
     * 根据 accountType 获取 HdTokenHelper，如果获取不到则根据 accountType 创建
     *
     * @param accountType 账号类型
     * @return HdTokenHelper
     */
    public static HdTokenHelper tokenHelper(String accountType) {
        return TOKEN_HELPER_MAP.computeIfAbsent(accountType, key -> HdSecurityHelperCreateStrategy.instance.getCreateTokenHelper().apply(key));
    }

    /**
     * 获取 HdBanAccountHelper，如果获取不到则根据 accountType 创建
     *
     * @return HdBanAccountHelper
     */
    public static HdBanAccountHelper banAccountHelper() {
        return banAccountHelper(ACCOUNT_TYPE);
    }

    /**
     * 根据 accountType 获取 HdBanAccountHelper，如果获取不到则根据 accountType 创建
     *
     * @param accountType 账号类型
     * @return HdBanAccountHelper
     */
    public static HdBanAccountHelper banAccountHelper(String accountType) {
        return BAN_ACCOUNT_HELPER_MAP.computeIfAbsent(accountType, key -> HdSecurityHelperCreateStrategy.instance.getCreateBanAccountHelper().apply(key));
    }

    /**
     * 获取 HdAuthorizeHelper，如果获取不到则根据 accountType 创建
     *
     * @return HdAuthorizeHelper
     */
    public static HdAuthorizeHelper authorizeHelper() {
        return authorizeHelper(ACCOUNT_TYPE);
    }

    /**
     * 根据 accountType 获取 HdAuthorizeHelper，如果获取不到则根据 accountType 创建
     *
     * @param accountType 账号类型
     * @return HdAuthorizeHelper
     */
    public static HdAuthorizeHelper authorizeHelper(String accountType) {
        return AUTHORIZE_HELPER_MAP.computeIfAbsent(accountType, key -> HdSecurityHelperCreateStrategy.instance.getCreateAuthorizeHelper().apply(key));
    }

    /**
     * 获取 HdAnnotationHelper，如果获取不到则创建
     *
     * @return HdAnnotationHelper
     */
    public static HdAnnotationHelper annotationHelper() {
        if (null == hdAnnotationHelper) {
            hdAnnotationHelper = HdSecurityHelperCreateStrategy.instance.getCreateAnnotationHelper().get();
        }
        return hdAnnotationHelper;
    }

    /**
     * 获取 HdBasicAuthHelper，如果获取不到则创建
     *
     * @return HdBasicAuthHelper
     */
    public static HdBasicAuthHelper basicAuthHelper() {
        if (null == hdBasicAuthHelper) {
            hdBasicAuthHelper = HdSecurityHelperCreateStrategy.instance.getCreateBasicAuthHelper().get();
        }
        return hdBasicAuthHelper;
    }

    /**
     * 获取 HdSameOriginTokenHelper，如果获取不到则创建
     *
     * @return HdSameOriginTokenHelper
     */
    public static HdSameOriginTokenHelper sameOriginTokenHelper() {
        if (null == hdSameOriginTokenHelper) {
            hdSameOriginTokenHelper = HdSecurityHelperCreateStrategy.instance.getCreateSameOriginTokenHelper().get();
        }
        return hdSameOriginTokenHelper;
    }

    // ---------- 代理默认账号的常用方法，具体用法代理方法的注释 ----------

    public static String login(Object loginId) {
        return loginHelper(ACCOUNT_TYPE).login(loginId);
    }

    public static String login(Object loginId, HdLoginModel loginModel) {
        return loginHelper(loginModel.getAccountType()).login(loginModel.setLoginId(loginId));
    }

    public static Object getLoginId() {
        return loginHelper(ACCOUNT_TYPE).getLoginId();
    }

    public static boolean isLogin() {
        return loginHelper(ACCOUNT_TYPE).isLogin();
    }

    public static void checkLogin() {
        loginHelper(ACCOUNT_TYPE).checkLogin();
    }

    public static void logout(Object loginId) {
        loginHelper(ACCOUNT_TYPE).logout(loginId);
    }

    public static void kickout(Object loginId) {
        loginHelper(ACCOUNT_TYPE).kickout(loginId);
    }

    public static void replaced(Object loginId) {
        loginHelper(ACCOUNT_TYPE).replaced(loginId);
    }

    public static HdAccountSession getAccountSession() {
        return sessionHelper(ACCOUNT_TYPE).getAccountSession();
    }

    public static HdTokenSession getTokenSession() {
        return sessionHelper(ACCOUNT_TYPE).getTokenSession();
    }

    public static String getWebToken() {
        return tokenHelper(ACCOUNT_TYPE).getWebToken();
    }

    public static String getTokenFromWeb() {
        return tokenHelper(ACCOUNT_TYPE).getTokenFromWeb();
    }

    public static void checkTokenActiveTime() {
        tokenHelper(ACCOUNT_TYPE).checkTokenActiveTime();
    }

    public static void updateLastActiveToNow() {
        tokenHelper(ACCOUNT_TYPE).updateTokenLastActiveTimeToNow();
    }

}
