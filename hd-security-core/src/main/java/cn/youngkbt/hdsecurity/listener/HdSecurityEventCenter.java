package cn.youngkbt.hdsecurity.listener;

import cn.youngkbt.hdsecurity.annotation.handler.HdAnnotationHandler;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityEventException;
import cn.youngkbt.hdsecurity.listener.impl.HdSecurityEventListenerForLog;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;

import java.lang.annotation.Annotation;
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

    /**
     * 发布事件
     *
     * @param listenerConsumer 事件处理器
     */
    public static void publishEvent(Consumer<HdSecurityEventListener> listenerConsumer) {
        for (HdSecurityEventListener listener : listenerList) {
            listenerConsumer.accept(listener);
        }
    }

    /**
     * 发布配置加载前置事件
     */
    public static void publishBeforeLoadConfig() {
        publishEvent(HdSecurityEventBeforeListener::beforeLoadConfig);
    }

    /**
     * 发布配置加载后置事件
     *
     * @param accountType      账号类型
     * @param hdSecurityConfig Hd Security 配置对象
     */
    public static void publishAfterLoadConfig(String accountType, HdSecurityConfig hdSecurityConfig) {
        publishEvent(listener -> listener.afterLoadConfig(accountType, hdSecurityConfig));
    }

    /**
     * 发布登录前置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     */
    public static void publishBeforeLogin(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeLogin(accountType, loginId));
    }

    /**
     * 发布登录后置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param token       登录凭证
     * @param loginModel  登录信息
     */
    public static void publishAfterLogin(String accountType, Object loginId, String token, HdLoginModel loginModel) {
        publishEvent(listener -> listener.afterLogin(accountType, loginId, token, loginModel));
    }

    /**
     * 发布注销前置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     */
    public static void publishBeforeLogout(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeLogout(accountType, loginId));
    }

    /**
     * 发布注销后置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param token       登录凭证
     */
    public static void publishAfterLogout(String accountType, Object loginId, String token) {
        publishEvent(listener -> listener.afterLogout(accountType, loginId, token));
    }

    /**
     * 发布替人下线前置事件
     *
     * @param accountType 账号类型
     * @param loginId     录 ID
     */
    public static void publishBeforeKickout(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeKickout(accountType, loginId));
    }

    /**
     * 发布替人下线后置事件
     *
     * @param accountType 账号类型
     * @param loginId     录 ID
     * @param token       登录凭证
     */
    public static void publishAfterKickout(String accountType, Object loginId, String token) {
        publishEvent(listener -> listener.afterKickout(accountType, loginId, token));
    }

    /**
     * 发布顶人下线前置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     */
    public static void publishBeforeReplaced(String accountType, Object loginId) {
        publishEvent(listener -> listener.beforeReplaced(accountType, loginId));
    }

    /**
     * 发布顶人下线后置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param token       登录凭证
     */
    public static void publishAfterReplaced(String accountType, Object loginId, String token) {
        publishEvent(listener -> listener.afterReplaced(accountType, loginId, token));
    }

    /**
     * 发布封禁账号前置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param disableTime 封禁时长
     * @param realm       封禁领域
     * @param level       封禁级别
     */
    public static void publishBeforeBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        publishEvent(listener -> listener.beforeBanAccount(accountType, loginId, disableTime, realm, level));
    }

    /**
     * 发布封禁账号后置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param disableTime 封禁时长
     * @param realm       封禁领域
     * @param level       封禁级别
     */
    public static void publishAfterBanAccount(String accountType, Object loginId, long disableTime, String realm, int level) {
        publishEvent(listener -> listener.afterBanAccount(accountType, loginId, disableTime, realm, level));
    }

    /**
     * 发布解封账号前置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param realm       封禁领域
     */
    public static void publishBeforeUnBanAccount(String accountType, Object loginId, String realm) {
        publishEvent(listener -> listener.beforeUnBanAccount(accountType, loginId, realm));
    }

    /**
     * 发布解封账号后置事件
     *
     * @param accountType 账号类型
     * @param loginId     登录 ID
     * @param realm       封禁领域
     */
    public static void publishAfterUnBanAccount(String accountType, Object loginId, String realm) {
        publishEvent(listener -> listener.afterUnBanAccount(accountType, loginId, realm));
    }

    /**
     * 发布创建会话前置事件
     *
     * @param sessionId 会话 ID
     */
    public static void publishBeforeCreateSession(String sessionId) {
        publishEvent(listener -> listener.beforeCreateSession(sessionId));
    }

    /**
     * 发布创建会话后置事件
     *
     * @param sessionId 会话 ID
     */
    public static void publishAfterCreateSession(String sessionId) {
        publishEvent(listener -> listener.afterCreateSession(sessionId));
    }

    /**
     * 发布注销会话前置事件
     *
     * @param sessionId 会话 ID
     */
    public static void publishBeforeLogoutSession(String sessionId) {
        publishEvent(listener -> listener.beforeLogoutSession(sessionId));
    }

    /**
     * 发布注销会话后置事件
     *
     * @param sessionId 会话 ID
     */
    public static void publishAfterLogoutSession(String sessionId) {
        publishEvent(listener -> listener.afterLogoutSession(sessionId));
    }

    /**
     * 发布 Token 续期前置事件
     *
     * @param token      Token
     * @param loginId    登录 ID
     * @param expireTime 过期时间
     */
    public static void publishBeforeRenewExpireTime(String token, Object loginId, long expireTime) {
        publishEvent(listener -> listener.beforeRenewExpireTime(token, loginId, expireTime));
    }

    /**
     * 发布 Token 续期后置事件
     *
     * @param token      Token
     * @param loginId    登录 ID
     * @param expireTime 过期时间
     */
    public static void publishAfterRenewExpireTime(String token, Object loginId, long expireTime) {
        publishEvent(listener -> listener.afterRenewExpireTime(token, loginId, expireTime));
    }

    /**
     * 发布二次认证开启前置事件
     *
     * @param accountType    账号类型
     * @param webToken       Web Token
     * @param realm          领域
     * @param secondAuthTime 二次认证时间
     */
    public static void publishBeforeSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
        publishEvent(listener -> listener.beforeSecondAuthOpen(accountType, webToken, realm, secondAuthTime));
    }

    /**
     * 发布二次认证开启后置事件
     *
     * @param accountType    账号类型
     * @param webToken       Web Token
     * @param realm          领域
     * @param secondAuthTime 二次认证时间
     */
    public static void publishAfterSecondAuthOpen(String accountType, String webToken, String realm, long secondAuthTime) {
        publishEvent(listener -> listener.afterSecondAuthOpen(accountType, webToken, realm, secondAuthTime));
    }

    /**
     * 发布二次认证关闭前置事件
     *
     * @param accountType 账号类型
     * @param webToken    Web Token
     * @param realm       领域
     */
    public static void publishBeforeSecondAuthClose(String accountType, String webToken, String realm) {
        publishEvent(listener -> listener.beforeSecondAuthClose(accountType, webToken, realm));
    }

    /**
     * 发布二次认证关闭后置事件
     *
     * @param accountType 账号类型
     * @param webToken    Web Token
     * @param realm       领域
     */
    public static void publishAfterSecondAuthClose(String accountType, String webToken, String realm) {
        publishEvent(listener -> listener.afterSecondAuthClose(accountType, webToken, realm));
    }

    /**
     * 发布组件注册前置事件
     *
     * @param componentName   组件名称
     * @param componentObject 组件对象
     */
    public static void publishBeforeComponentRegister(String componentName, Object componentObject) {
        publishEvent(listener -> listener.beforeComponentRegister(componentName, componentObject));
    }

    /**
     * 发布组件注册后置事件
     *
     * @param componentName   组件名称
     * @param componentObject 组件对象
     */
    public static void publishAfterComponentRegister(String componentName, Object componentObject) {
        publishEvent(listener -> listener.afterComponentRegister(componentName, componentObject));
    }

    /**
     * 发布注解处理器注册前置事件
     *
     * @param annotationHandler 注解处理器
     */
    public static void publishBeforeRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
        publishEvent(listener -> listener.beforeRegisterAnnotationHandler(annotationHandler));
    }

    /**
     * 发布注解处理器注册后置事件
     *
     * @param annotationHandler 注解处理器
     */
    public static void publishAfterRegisterAnnotationHandler(HdAnnotationHandler<? extends Annotation> annotationHandler) {
        publishEvent(listener -> listener.afterRegisterAnnotationHandler(annotationHandler));
    }
}
