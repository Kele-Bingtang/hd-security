package cn.youngkbt.hdsecurity.authorize;

import java.util.List;

/**
 * 权限数据获取接口的默认实现类 {@link HdSecurityAuthorize}，框架默认使用该实现类返回空集合，即用户不具有任何权限和角色
 *
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
