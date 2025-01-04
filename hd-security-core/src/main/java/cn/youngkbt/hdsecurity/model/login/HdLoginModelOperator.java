package cn.youngkbt.hdsecurity.model.login;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;

import java.util.Optional;

/**
 * Hd Security 登录模型操作类
 *
 * @author Tianke
 * @date 2024/11/27 22:46:53
 * @since 1.0.0
 */
public class HdLoginModelOperator {
    private HdLoginModelOperator() {
    }

    public static HdLoginModel create() {
        return new HdLoginModel();
    }

    public static HdLoginModel build() {
        return build(HdSecurityManager.getConfig());
    }

    public static HdLoginModel build(HdSecurityConfig hdSecurityConfig) {
        return create()
                .setWriteHeader(hdSecurityConfig.getWriteHeader())
                .setTokenExpireTime(hdSecurityConfig.getTokenExpireTime())
                .setTokenActiveExpireTime(Boolean.TRUE.equals(hdSecurityConfig.getDynamicActiveExpireTime()) ? hdSecurityConfig.getTokenActiveExpireTime() : null);
    }

    public static HdLoginModel mutate(HdLoginModel hdLoginModel) {
        HdSecurityConfig config = HdSecurityManager.getConfig(hdLoginModel.getAccountType());
        if (null == hdLoginModel.getWriteHeader()) {
            hdLoginModel.setWriteHeader(config.getWriteHeader());
        }

        if (null == hdLoginModel.getTokenExpireTime()) {
            hdLoginModel.setTokenExpireTime(config.getTokenExpireTime());
        }

        if (Boolean.TRUE.equals(config.getDynamicActiveExpireTime()) && null == hdLoginModel.getTokenActiveExpireTime()) {
            hdLoginModel.setTokenActiveExpireTime(config.getTokenActiveExpireTime());
        }

        return hdLoginModel;
    }

    public static Long getTokenExpireTimeout(HdLoginModel hdLoginModel) {
        return Optional.ofNullable(hdLoginModel.getTokenExpireTime()).orElse(HdSecurityManager.getConfig(hdLoginModel.getAccountType()).getTokenExpireTime());
    }

    public static Long getTokenActiveExpireTime(HdLoginModel hdLoginModel) {
        return Optional.ofNullable(hdLoginModel.getTokenActiveExpireTime()).orElse(HdSecurityManager.getConfig(hdLoginModel.getAccountType()).getTokenActiveExpireTime());
    }

    public static Boolean getWriteHeader(HdLoginModel hdLoginModel) {
        return Optional.ofNullable(hdLoginModel.getWriteHeader()).orElse(HdSecurityManager.getConfig(hdLoginModel.getAccountType()).getWriteHeader());
    }

}
