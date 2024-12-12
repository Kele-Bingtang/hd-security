package cn.youngkbt.hdsecurity.authorize;

/**
 * @author Tianke
 * @date 2024/12/12 23:09:39
 * @since 1.0.0
 */
public class HdSecurityAuthorizeProvider {

    private HdSecurityAuthorizeProvider() {
    }
    
    private static HdSecurityAuthorize hdSecurityAuthorize = new HdSecurityAuthorizeForSimple();
    
    public static HdSecurityAuthorize getHdSecurityAuthorize() {
        return hdSecurityAuthorize;
    }
    
    public static void setHdSecurityAuthorize(HdSecurityAuthorize hdSecurityAuthorize) {
        HdSecurityAuthorizeProvider.hdSecurityAuthorize = hdSecurityAuthorize;
    }
}
