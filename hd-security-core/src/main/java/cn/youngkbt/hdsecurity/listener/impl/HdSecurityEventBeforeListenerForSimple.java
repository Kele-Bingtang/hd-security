package cn.youngkbt.hdsecurity.listener.impl;

import cn.youngkbt.hdsecurity.listener.HdSecurityEventBeforeListener;

/**
 * @author Tianke
 * @date 2024/11/25 21:51:39
 * @since 1.0.0
 */
public class HdSecurityEventBeforeListenerForSimple implements HdSecurityEventBeforeListener {
    @Override
    public void beforeLoadConfig() {
        
    }

    @Override
    public void beforeLogin(String accountType, Object loginId) {
        
    }

    @Override
    public void beforeLogout(String accountType, Object loginId) {

    }

    @Override
    public void beforeKickout(String accountType, Object loginId) {

    }

    @Override
    public void beforeReplaced(String accountType, Object loginId) {

    }

    @Override
    public void beforeBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        
    }

    @Override
    public void beforeUnBanAccount(String accountType, Object loginId, String realm) {

    }

    @Override
    public void beforeCreateSession(String sessionId) {

    }

    @Override
    public void beforeLogoutSession(String sessionId) {

    }

    @Override
    public void beforeRenewExpireTime(String token, Object loginId, long expireTime) {

    }

    @Override
    public void beforeSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
        
    }

    @Override
    public void beforeSecondAuthClose(String accountType, String webToken, String realm) {
        
    }

    @Override
    public void beforeComponentRegister(String componentName, Object componentObject) {
        
    }
}
