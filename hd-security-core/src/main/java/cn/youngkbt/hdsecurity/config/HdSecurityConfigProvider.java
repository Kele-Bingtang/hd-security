package cn.youngkbt.hdsecurity.config;

import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HdSecurity 配置信息提供者
 * HdSecurity 配置信息不会在项目启动的时候初始化读取，而是显式调用 getHdSecurityConfig 方法后才去初始化读取配置信息。其中 HdLoginUtil.login(...) 方法内会调用 getHdSecurityConfig 方法
 *
 * @author Tianke
 * @date 2024/11/25 23:59:24
 * @since 1.0.0
 */
public class HdSecurityConfigProvider {

    public static final Map<String, HdSecurityConfig> accountTypeConfigMap = new ConcurrentHashMap<>();

    private HdSecurityConfigProvider() {
    }

    /**
     * 获取 HdSecurity 配置信息对象，如果为空，则尝试从 resource 下读取
     * 因为读取配置信息耗费些时间，因此加锁避免多次调用导致重复读取
     *
     * @return HdSecurity 配置信息对象
     */
    public static HdSecurityConfig getHdSecurityConfig() {
        return getHdSecurityConfig(DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public static HdSecurityConfig getHdSecurityConfig(String accountType) {
        HdSecurityConfig hdSecurityConfig = accountTypeConfigMap.get(accountType);
        // 如果 config 为空，则创建 config，并保存到 accountTypeConfigMap
        if (null == hdSecurityConfig) {
            synchronized (HdSecurityConfigProvider.class) {
                if (null == hdSecurityConfig) {
                    // 发布前置事件
                    HdSecurityEventCenter.publishBeforeLoadConfig();
                    // 创建 config 配置信息
                    HdSecurityConfig config = HdSecurityConfigFactory.createConfig();
                    setHdSecurityConfig(accountType, config);
                    return config;
                }
            }
        }
        return hdSecurityConfig;
    }


    /**
     * 设置 HdSecurity 配置信息对象
     *
     * @param hdSecurityConfig HdSecurity 配置信息对象
     */
    public static void setHdSecurityConfig(HdSecurityConfig hdSecurityConfig) {
        setHdSecurityConfig(DefaultConstant.DEFAULT_ACCOUNT_TYPE, hdSecurityConfig);
    }

    /**
     * 设置 HdSecurity 配置信息对象
     *
     * @param hdSecurityConfig HdSecurity 配置信息对象
     */
    public static void setHdSecurityConfig(String accountType, HdSecurityConfig hdSecurityConfig) {
        accountTypeConfigMap.put(accountType, hdSecurityConfig);
        // 发布事件
        HdSecurityEventCenter.publishAfterLoadConfig(accountType, hdSecurityConfig);
    }

    /**
     * 是否启用 Token 活跃时间 & 冻结功能
     *
     * @return true 启用 Token 活跃时间 & 冻结功能
     */
    public static boolean isUseActiveExpireTime() {
        HdSecurityConfig config = getHdSecurityConfig();
        return config.getTokenActiveExpireTime() != HdSecurityRepositoryKV.NEVER_EXPIRE || config.getDynamicActiveExpireTime();
    }
}
