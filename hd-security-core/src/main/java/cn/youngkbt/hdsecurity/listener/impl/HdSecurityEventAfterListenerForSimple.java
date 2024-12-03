package cn.youngkbt.hdsecurity.listener.impl;

import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventAfterListener;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;

/**
 * @author Tianke
 * @date 2024/11/25 21:51:06
 * @since 1.0.0
 */
public class HdSecurityEventAfterListenerForSimple implements HdSecurityEventAfterListener {

    @Override
    public void afterLoadConfig(HdSecurityConfig hdSecurityConfig) {
        
    }

    @Override
    public void afterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel) {
        
    }

    @Override
    public void afterLogout(String accountType, Object loginId, String token) {

    }

    @Override
    public void afterKickout(String accountType, Object loginId, String token) {

    }

    @Override
    public void afterReplaced(String accountType, Object loginId, String token) {

    }

    @Override
    public void afterCreateSession(String sessionId) {

    }

    @Override
    public void afterLogoutSession(String sessionId) {

    }

    @Override
    public void afterRenewExpireTime(String token, Object loginId, long expireTime) {

    }

    @Override
    public void afterComponentRegister(String componentName, Object componentObject) {
        
    }
}
