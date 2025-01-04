package cn.youngkbt.hdsecurity.listener.impl;

import cn.youngkbt.hdsecurity.annotation.handler.HdAnnotationHandler;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventListener;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;

import java.lang.annotation.Annotation;

/**
 * Hd Security 事件监听器默认实现类，可以继承该类重写需要的事件监听方法
 *
 * @author Tianke
 * @date 2025/1/5 00:48:29
 * @since 1.0.0
 */
public class HdSecurityEventListenerForSimple implements HdSecurityEventListener {
    @Override
    public void beforeLoadConfig() {
    }

    @Override
    public void afterLoadConfig(String accountType, HdSecurityConfig hdSecurityConfig) {
    }

    @Override
    public void beforeLogin(String accountType, Object loginId) {
    }

    @Override
    public void afterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel) {
    }

    @Override
    public void beforeLogout(String accountType, Object loginId) {
    }

    @Override
    public void afterLogout(String accountType, Object loginId, String token) {
    }

    @Override
    public void beforeKickout(String accountType, Object loginId) {
    }

    @Override
    public void afterKickout(String accountType, Object loginId, String token) {
    }

    @Override
    public void beforeReplaced(String accountType, Object loginIdn) {
    }

    @Override
    public void afterReplaced(String accountType, Object loginId, String token) {
    }

    @Override
    public void beforeBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
    }

    @Override
    public void afterBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
    }


    @Override
    public void beforeUnBanAccount(String accountType, Object loginId, String realm) {
    }

    @Override
    public void afterUnBanAccount(String accountType, Object loginId, String realm) {
    }

    @Override
    public void beforeCreateSession(String sessionId) {
    }

    @Override
    public void afterCreateSession(String sessionId) {
    }

    @Override
    public void beforeLogoutSession(String sessionId) {
    }

    @Override
    public void afterLogoutSession(String sessionId) {
    }

    @Override
    public void beforeRenewExpireTime(String token, Object loginId, long expireTime) {
    }

    @Override
    public void afterRenewExpireTime(String token, Object loginId, long expireTime) {
    }

    @Override
    public void beforeSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
    }

    @Override
    public void afterSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
    }

    @Override
    public void beforeSecondAuthClose(String accountType, String token, String realm) {
    }

    @Override
    public void afterSecondAuthClose(String accountType, String token, String realm) {
    }

    @Override
    public void beforeComponentRegister(String componentName, Object componentObject) {
    }

    @Override
    public void afterComponentRegister(String componentName, Object componentObject) {
    }

    @Override
    public void beforeRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
    }

    @Override
    public void afterRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
    }
}
