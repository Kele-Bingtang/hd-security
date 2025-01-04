package cn.youngkbt.hdsecurity.authorize;

/**
 * 对外提供权限数据实现类，如果需要自定义权限数据实现类，可以覆盖默认实现类
 *
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
