package cn.youngkbt.hdsecurity.config;

import cn.youngkbt.hdsecurity.utils.PropertiesUtil;

import java.util.Optional;

/**
 * Hd Security 配置类构建工厂
 * 扫描 resource 下的配置文件，并构建配置文件对象 HdSecurityConfig 对象。仅在非 IOC 环境下使用
 *
 * @author Tianke
 * @date 2024/11/25 00:51:43
 * @since 1.0.0
 */
public class HdSecurityConfigFactory {

    private HdSecurityConfigFactory() {
    }

    /**
     * 配置文件地址，默认为 resource 下的 hd-security.properties
     */
    private static final String CONFIG_PATH = "hd-security.properties";

    /**
     * 创建配置文件对象
     *
     * @return HdSecurity 配置对象
     */
    public static HdSecurityConfig createConfig() {
        return createConfig(CONFIG_PATH);
    }

    /**
     * 创建配置文件对象
     *
     * @param path 配置文件路径
     * @return HdSecurity 配置对象
     */
    public static HdSecurityConfig createConfig(String path) {
        return Optional.ofNullable(PropertiesUtil.readerThenConvert(path, HdSecurityConfig.class)).orElse(new HdSecurityConfig());
    }
}
