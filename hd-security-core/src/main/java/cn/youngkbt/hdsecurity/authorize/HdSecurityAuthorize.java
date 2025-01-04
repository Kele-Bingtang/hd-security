package cn.youngkbt.hdsecurity.authorize;

import java.util.List;

/**
 * 权限数据获取接口
 *
 * @author Tianke
 * @date 2024/12/12 22:55:15
 * @since 1.0.0
 */
public interface HdSecurityAuthorize {

    /**
     * 返回指定账号 ID 拥有的权限码列表
     *
     * @param loginId     账号id
     * @param accountType 账号类型
     * @return 该账号 ID 拥有的权限码集合
     */
    List<String> getPermissionList(Object loginId, String accountType);

    /**
     * 返回指定账号 ID 拥有的角色码列表
     *
     * @param loginId     账号id
     * @param accountType 账号类型
     * @return 该账号 ID 拥有的角色列表
     */
    List<String> getRoleList(Object loginId, String accountType);
}
