package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.strategy.HelperCreateStrategy;

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
    /**
     * 默认的 accountType
     */
    private static final String ACCOUNT_TYPE = DefaultConstant.DEFAULT_ACCOUNT_TYPE;

    public static HdLoginHelper loginHelper() {
        return loginHelper(ACCOUNT_TYPE);
    }

    public static HdLoginHelper loginHelper(String accountType) {
        // 根据 accountType 获取 HdSessionHelper，如果获取不到则根据 accountType 创建
        return LOGIN_HELPER_MAP.computeIfAbsent(accountType, key -> HelperCreateStrategy.instance.createLoginHelper.apply(key));
    }

    public static HdSessionHelper sessionHelper() {
        return sessionHelper(ACCOUNT_TYPE);
    }

    public static HdSessionHelper sessionHelper(String accountType) {
        // 根据 accountType 获取登 HdSessionHelper，如果获取不到则根据 accountType 创建
        return SESSION_HELPER_MAP.computeIfAbsent(accountType, key -> HelperCreateStrategy.instance.createSessionHelper.apply(key));
    }

    public static HdTokenHelper tokenHelper() {
        return tokenHelper(ACCOUNT_TYPE);
    }

    public static HdTokenHelper tokenHelper(String accountType) {
        // 根据 accountType 获取登 HdTokenHelper，如果获取不到则根据 accountType 创建
        return TOKEN_HELPER_MAP.computeIfAbsent(accountType, key -> HelperCreateStrategy.instance.createTokenHelper.apply(key));
    }

    // ---------- 代理常用方法 ----------
    public static String login(Object loginId) {
        return loginHelper(ACCOUNT_TYPE).login(loginId);
    }

    public static String login(Object loginId, HdLoginModel loginModel) {
        return loginHelper(loginModel.getAccountType()).login(loginModel.setLoginId(loginId));
    }

    public static void logout(Object loginId, String device) {
        loginHelper().logout(loginId, device);
    }
}
