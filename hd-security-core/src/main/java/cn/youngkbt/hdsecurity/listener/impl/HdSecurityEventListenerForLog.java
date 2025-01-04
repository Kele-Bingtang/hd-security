package cn.youngkbt.hdsecurity.listener.impl;

import cn.youngkbt.hdsecurity.GlobalEventEnums;
import cn.youngkbt.hdsecurity.annotation.handler.HdAnnotationHandler;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventListener;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.utils.HdDateUtil;

import java.lang.annotation.Annotation;
import java.util.EnumMap;
import java.util.Map;

import static cn.youngkbt.hdsecurity.log.HdSecurityLogProvider.log;

/**
 * Hd Security 前置和后置事件监听器日志打印实现类，使用 Log 打印事件
 *
 * @author Tianke
 * @date 2024/11/25 21:55:18
 * @since 1.0.0
 */
public class HdSecurityEventListenerForLog implements HdSecurityEventListener {

    private final static Map<GlobalEventEnums, Long> startCostTimeMap = new EnumMap<>(GlobalEventEnums.class);

    public static Map<GlobalEventEnums, Long> getStartCostTimeMap() {
        return startCostTimeMap;
    }

    @Override
    public void beforeLoadConfig() {
        startCostTimeMap.put(GlobalEventEnums.LOAD_CONFIG, System.currentTimeMillis());
    }

    @Override
    public void afterLoadConfig(String accountType, HdSecurityConfig hdSecurityConfig) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.LOAD_CONFIG, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.LOAD_CONFIG, currentTimeMillis));
        log.info("账号类型【" + accountType + "】全局配置加载成功 {}", hdSecurityConfig);
    }

    @Override
    public void beforeLogin(String accountType, Object loginId) {
        startCostTimeMap.put(GlobalEventEnums.LOGIN, System.currentTimeMillis());
    }

    @Override
    public void afterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.LOGIN, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.LOGIN, currentTimeMillis));
        log.info("账号 {} 登录成功，账号类型 accountType = {}, 会话凭证 token = {}", loginId, accountType, token);
    }

    @Override
    public void beforeLogout(String accountType, Object loginId) {
        startCostTimeMap.put(GlobalEventEnums.LOGOUT, System.currentTimeMillis());
    }

    @Override
    public void afterLogout(String accountType, Object loginId, String token) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.LOGOUT, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.LOGOUT, currentTimeMillis));
        log.info("账号 {} 注销登录，账号类型 accountType = {}, 会话凭证 token = {}", loginId, accountType, token);
    }

    @Override
    public void beforeKickout(String accountType, Object loginId) {
        startCostTimeMap.put(GlobalEventEnums.KICKOUT, System.currentTimeMillis());
    }

    @Override
    public void afterKickout(String accountType, Object loginId, String token) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.KICKOUT, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.KICKOUT, currentTimeMillis));
        log.info("账号 {} 被踢下线，账号类型 accountType = {}, 会话凭证 token = {}", loginId, accountType, token);
    }

    @Override
    public void beforeReplaced(String accountType, Object loginIdn) {
        startCostTimeMap.put(GlobalEventEnums.REPLACED, System.currentTimeMillis());
    }

    @Override
    public void afterReplaced(String accountType, Object loginId, String token) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.REPLACED, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.REPLACED, currentTimeMillis));
        log.info("账号 {} 被顶下线，账号类型 accountType = {}, 会话凭证 token = {}", loginId, accountType, token);
    }

    @Override
    public void beforeBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        startCostTimeMap.put(GlobalEventEnums.BAN, System.currentTimeMillis());
    }

    @Override
    public void afterBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.BAN, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.BAN, currentTimeMillis));
        log.info("账号 {} 在 {} 领域被封禁，账号类型 = {}, 封禁等级 = {}, 解封时间 = {}", loginId, accountType, realm, level, HdDateUtil.formatDateTime(disableTime * 1000));
    }


    @Override
    public void beforeUnBanAccount(String accountType, Object loginId, String realm) {
        startCostTimeMap.put(GlobalEventEnums.UN_BAN, System.currentTimeMillis());
    }

    @Override
    public void afterUnBanAccount(String accountType, Object loginId, String realm) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.UN_BAN, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.UN_BAN, currentTimeMillis));
        log.info("账号 {} 在 {} 领域解封成功，账号类型 = {}", loginId, realm, accountType);
    }

    @Override
    public void beforeCreateSession(String sessionId) {
        startCostTimeMap.put(GlobalEventEnums.CREATE_SESSION, System.currentTimeMillis());
    }

    @Override
    public void afterCreateSession(String sessionId) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.CREATE_SESSION, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.CREATE_SESSION, currentTimeMillis));
        log.info("HdSession [{}] 创建成功", sessionId);
    }

    @Override
    public void beforeLogoutSession(String sessionId) {
        startCostTimeMap.put(GlobalEventEnums.LOGOUT_SESSION, System.currentTimeMillis());
    }

    @Override
    public void afterLogoutSession(String sessionId) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.LOGOUT_SESSION, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.LOGOUT_SESSION, currentTimeMillis));
        log.info("HdSession [{}] 注销成功", sessionId);
    }

    @Override
    public void beforeRenewExpireTime(String token, Object loginId, long expireTime) {
        startCostTimeMap.put(GlobalEventEnums.RENEW_EXPIRE_TIME, System.currentTimeMillis());
    }

    @Override
    public void afterRenewExpireTime(String token, Object loginId, long expireTime) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.RENEW_EXPIRE_TIME, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.RENEW_EXPIRE_TIME, currentTimeMillis));
        log.info("token 续期成功, {} 秒后到期, 帐号 = {}, token = {} ", expireTime, loginId, token);
    }

    @Override
    public void beforeSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
        startCostTimeMap.put(GlobalEventEnums.SECOND_AUTH_OPEN, System.currentTimeMillis());
    }

    @Override
    public void afterSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.SECOND_AUTH_OPEN, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.SECOND_AUTH_OPEN, currentTimeMillis));
        log.info("账号类型 {} 的 token 二级认证成功, 领域标识 = {}, 有效期 = {} 秒, Token：{}", accountType, realm, secondAuthTime, webToken);
    }

    @Override
    public void beforeSecondAuthClose(String accountType, String token, String realm) {
        startCostTimeMap.put(GlobalEventEnums.SECOND_AUTH_CLOSE, System.currentTimeMillis());
    }

    @Override
    public void afterSecondAuthClose(String accountType, String token, String realm) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.SECOND_AUTH_CLOSE, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.SECOND_AUTH_CLOSE, currentTimeMillis));
        log.info("账号类型 {} 的 token 二级认证关闭, 领域标识 = {}, Token = {}", accountType, realm, token);
    }

    @Override
    public void beforeComponentRegister(String componentName, Object componentObject) {
        startCostTimeMap.put(GlobalEventEnums.getByFunctionName(componentName), System.currentTimeMillis());
    }

    @Override
    public void afterComponentRegister(String componentName, Object componentObject) {
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.getByFunctionName(componentName), currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.REGISTER_LOG, currentTimeMillis));
        String canonicalName = componentName == null ? null : componentObject.getClass().getCanonicalName();
        log.info("全局组件 {} 载入成功: {}", componentName, canonicalName);
    }

    @Override
    public void beforeRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
        startCostTimeMap.put(GlobalEventEnums.REGISTER_ANNOTATION_HANDLER, System.currentTimeMillis());
    }

    @Override
    public void afterRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
        if (null == annotationHandler) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        startCostTimeMap.put(GlobalEventEnums.REGISTER_ANNOTATION_HANDLER, currentTimeMillis - startCostTimeMap.getOrDefault(GlobalEventEnums.REGISTER_ANNOTATION_HANDLER, currentTimeMillis));
        log.info("注解扩展 @{} (处理器: {})", annotationHandler.getHandlerAnnotationClass().getSimpleName(), annotationHandler.getClass().getCanonicalName());
    }
}
