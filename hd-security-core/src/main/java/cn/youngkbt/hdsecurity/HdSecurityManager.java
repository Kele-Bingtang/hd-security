package cn.youngkbt.hdsecurity;

import cn.youngkbt.hdsecurity.authorize.HdSecurityAuthorize;
import cn.youngkbt.hdsecurity.authorize.HdSecurityAuthorizeProvider;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.context.HdSecurityContext;
import cn.youngkbt.hdsecurity.context.HdSecurityContextProvider;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventListener;
import cn.youngkbt.hdsecurity.log.HdSecurityLog;
import cn.youngkbt.hdsecurity.log.HdSecurityLogProvider;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryProvider;

import java.util.List;

/**
 * Hd Security 全局管理类，管理所有全局组件，可通过此类快速获取、写入各种全局组件对象
 *
 * @author Tianke
 * @date 2024/11/25 22:01:33
 * @since 1.0.0
 */
public class HdSecurityManager {

    private HdSecurityManager() {
    }

    public static HdSecurityConfig getConfig() {
        return HdSecurityConfigProvider.getHdSecurityConfig();
    }

    public static HdSecurityConfig getConfig(String accountType) {
        return HdSecurityConfigProvider.getHdSecurityConfig(accountType);
    }

    public static void setConfig(HdSecurityConfig hdSecurityConfig) {
        HdSecurityConfigProvider.setHdSecurityConfig(hdSecurityConfig);
    }

    public static void setConfig(String accountType, HdSecurityConfig hdSecurityConfig) {
        HdSecurityConfigProvider.setHdSecurityConfig(accountType, hdSecurityConfig);
    }

    public static HdSecurityRepository getRepository() {
        return HdSecurityRepositoryProvider.getHdSecurityRepository();
    }

    public static void setRepository(HdSecurityRepository hdSecurityRepository) {
        HdSecurityRepositoryProvider.setHdSecurityRepository(hdSecurityRepository);
    }

    public static HdSecurityLog getLog() {
        return HdSecurityLogProvider.getLog();
    }

    public static void setLog(HdSecurityLog hdSecurityLog) {
        HdSecurityLogProvider.setLog(hdSecurityLog);
    }

    public static HdSecurityContext getContext() {
        return HdSecurityContextProvider.getHdSecurityContext();
    }

    public static void setContext(HdSecurityContext context) {
        HdSecurityContextProvider.setHdSecurityContext(context);
    }

    public static HdSecurityAuthorize getAuthorize() {
        return HdSecurityAuthorizeProvider.getHdSecurityAuthorize();
    }

    public static void setAuthorize(HdSecurityAuthorize hdSecurityAuthorize) {
        HdSecurityAuthorizeProvider.setHdSecurityAuthorize(hdSecurityAuthorize);
    }

    public static List<HdSecurityEventListener> getEventListener() {
        return HdSecurityEventCenter.getListenerList();
    }

    public static void setEventListener(List<HdSecurityEventListener> eventListener) {
        HdSecurityEventCenter.setListenerList(eventListener);
    }

    public static void addEventListener(HdSecurityEventListener eventListener) {
        HdSecurityEventCenter.addListener(eventListener);
    }
}
