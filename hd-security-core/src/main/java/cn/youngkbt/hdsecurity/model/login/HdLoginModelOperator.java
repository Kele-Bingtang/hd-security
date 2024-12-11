package cn.youngkbt.hdsecurity.model.login;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;

import java.util.Optional;

/**
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
                .setTokenActiveExpireTime(hdSecurityConfig.getTokenActiveExpireTime());
    }
    
    public static HdLoginModel mutate(HdLoginModel hdLoginModel) {
        if(null == hdLoginModel.getWriteHeader()) {
            hdLoginModel.setWriteHeader(HdSecurityManager.getConfig().getWriteHeader());
        }
        
        if(null == hdLoginModel.getTokenExpireTime()) {
            hdLoginModel.setTokenExpireTime(HdSecurityManager.getConfig().getTokenExpireTime());
        }
        
        if(null == hdLoginModel.getTokenActiveExpireTime()) {
            hdLoginModel.setTokenActiveExpireTime(HdSecurityManager.getConfig().getTokenActiveExpireTime());
        }
        
        return hdLoginModel;
    }

    public static Long getTokenExpireTimeout(HdLoginModel hdLoginModel) {
        return Optional.ofNullable(hdLoginModel.getTokenExpireTime()).orElse(HdSecurityManager.getConfig().getTokenExpireTime());
    }

    public static Long getTokenActiveExpireTime(HdLoginModel hdLoginModel) {
        return Optional.ofNullable(hdLoginModel.getTokenActiveExpireTime()).orElse(HdSecurityManager.getConfig().getTokenActiveExpireTime());
    }

    public static Boolean getWriteHeader(HdLoginModel hdLoginModel) {
        return Optional.ofNullable(hdLoginModel.getWriteHeader()).orElse(HdSecurityManager.getConfig().getWriteHeader());
    }

}
