package cn.youngkbt.hdsecurity.listener;

import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityEventException;
import cn.youngkbt.hdsecurity.listener.impl.HdSecurityEventListenerForLog;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Hd Security 事件发布中心
 *
 * @author Tianke
 * @date 2024/11/25 22:12:04
 * @since 1.0.0
 */
public class HdSecurityEventCenter {

    private HdSecurityEventCenter() {
    }

    private static List<HdSecurityEventListener> listenerList = HdCollectionUtil.newArrayList();

    static {
        // 默认添加控制台日志侦听器 
        listenerList.add(new HdSecurityEventListenerForLog());
    }

    /**
     * 获取当前侦听器集合
     *
     * @return 侦听器集合
     */
    public static List<HdSecurityEventListener> getListenerList() {
        return listenerList;
    }

    /**
     * 设置侦听器集合
     *
     * @param listenerList 侦听器集合
     */
    public static void setListenerList(List<HdSecurityEventListener> listenerList) {
        if (HdCollectionUtil.isEmpty(listenerList)) {
            throw new HdSecurityEventException("注册的侦听器集合不可以为空").setCode(HdSecurityErrorCode.LISTENERS_IS_NULL);
        }

        if (HdCollectionUtil.elementIsEmpty(listenerList)) {
            throw new HdSecurityEventException("注册的侦听器不可以为空").setCode(HdSecurityErrorCode.LISTENER_IS_NULL);
        }

        HdSecurityEventCenter.listenerList = listenerList;
        // 重新排序
        HdSecurityEventCenter.listenerList.sort(Comparator.comparingInt(HdSecurityEventListener::getOrder));
    }

    /**
     * 添加一个侦听器
     *
     * @param listener 侦听器
     */
    public static void addListener(HdSecurityEventListener listener) {
        listenerList.add(listener);
    }

    /**
     * 添加多个侦听器
     *
     * @param listenerList 侦听器集合
     */
    public static void addListener(List<HdSecurityEventListener> listenerList) {
        if (HdCollectionUtil.isEmpty(listenerList)) {
            throw new HdSecurityEventException("注册的侦听器集合不可以为空").setCode(HdSecurityErrorCode.LISTENERS_IS_NULL);
        }

        if (HdCollectionUtil.elementIsEmpty(listenerList)) {
            throw new HdSecurityEventException("注册的侦听器不可以为空").setCode(HdSecurityErrorCode.LISTENER_IS_NULL);
        }

        HdSecurityEventCenter.listenerList.addAll(listenerList);
        // 重新排序
        HdSecurityEventCenter.listenerList.sort(Comparator.comparingInt(HdSecurityEventListener::getOrder));
    }

    /**
     * 移除指定侦听器
     *
     * @param listener 侦听器
     */
    public static void removeListener(HdSecurityEventListener listener) {
        listenerList.remove(listener);
    }

    /**
     * 移除指定类型的所有侦听器
     *
     * @param listenerClass 侦听器类型
     */
    public static void removeListener(Class<? extends HdSecurityEventListener> listenerClass) {
        listenerList.removeIf(listener -> listenerClass.isAssignableFrom(listener.getClass()));
    }

    /**
     * 清空所有已注册的侦听器
     */
    public static void clearListener() {
        listenerList.clear();
    }

    public static void publishBeforeLoadConfig() {
        publishEvent(HdSecurityEventBeforeListener::beforeLoadConfig);
    }

    public static void publishAfterLoadConfig(String accountType, HdSecurityConfig hdSecurityConfig) {
        publishEvent(listener -> listener.afterLoadConfig(accountType, hdSecurityConfig));
    }

    public static void publishBeforeLogin(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeLogin(accountType, loginId));
    }

    public static void publishAfterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel) {
        publishEvent(listener -> listener.afterLogin(accountType, loginId, token, loginModel));
    }

    public static void publishBeforeLogout(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeLogout(accountType, loginId));
    }

    public static void publishAfterLogout(String accountType, Object loginId, String token) {
        publishEvent(listener -> listener.afterLogout(accountType, loginId, token));
    }

    public static void publishBeforeKickout(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeKickout(accountType, loginId));
    }

    public static void publishAfterKickout(String accountType, Object loginId, String token) {
        publishEvent(listener -> listener.afterKickout(accountType, loginId, token));
    }

    public static void publishBeforeReplaced(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeReplaced(accountType, loginId));
    }

    public static void publishAfterReplaced(String accountType, Object loginId, String token) {
        publishEvent(listener -> listener.afterReplaced(accountType, loginId, token));
    }

    public static void publishBeforeBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        publishEvent(listener -> listener.beforeBanAccount(accountType, loginId, disableTime, realm, level));
    }

    public static void publishAfterBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        publishEvent(listener -> listener.afterBanAccount(accountType, loginId, disableTime, realm, level));
    }

    public static void publishBeforeUnBanAccount(String accountType, Object loginId, String realm) {
        publishEvent(listener -> listener.beforeUnBanAccount(accountType, loginId, realm));
    }

    public static void publishAfterUnBanAccount(String accountType, Object loginId, String realm) {
        publishEvent(listener -> listener.afterUnBanAccount(accountType, loginId, realm));
    }

    public static void publishBeforeCreateSession(String sessionId) {
        publishEvent(listener -> listener.beforeCreateSession(sessionId));
    }

    public static void publishAfterCreateSession(String sessionId) {
        publishEvent(listener -> listener.afterCreateSession(sessionId));
    }

    public static void publishBeforeLogoutSession(String sessionId) {
        publishEvent(listener -> listener.beforeLogoutSession(sessionId));
    }

    public static void publishAfterLogoutSession(String sessionId) {
        publishEvent(listener -> listener.afterLogoutSession(sessionId));
    }

    public static void publishBeforeRenewExpireTime(String token, Object loginId, long expireTime) {
        publishEvent(listener -> listener.beforeRenewExpireTime(token, loginId, expireTime));
    }

    public static void publishAfterRenewExpireTime(String token, Object loginId, long expireTime) {
        publishEvent(listener -> listener.afterRenewExpireTime(token, loginId, expireTime));
    }

    public static void publishBeforeComponentRegister(String componentName, Object componentObject) {
        publishEvent(listener -> listener.beforeComponentRegister(componentName, componentObject));
    }

    public static void publishAfterComponentRegister(String componentName, Object componentObject) {
        publishEvent(listener -> listener.afterComponentRegister(componentName, componentObject));
    }

    public static void publishEvent(Consumer<HdSecurityEventListener> listenerConsumer) {
        for (HdSecurityEventListener listener : listenerList) {
            listenerConsumer.accept(listener);
        }
    }
}
