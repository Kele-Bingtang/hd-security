package cn.youngkbt.hdsecurity.authorize;

import java.util.List;

/**
 * @author Tianke
 * @date 2024/12/12 23:07:09
 * @since 1.0.0
 */
public class HdSecurityAuthorizeForSimple implements HdSecurityAuthorize {

    @Override
    public List<String> getPermissionList(Object loginId, String accountType) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String accountType) {
        return List.of();
    }
}
