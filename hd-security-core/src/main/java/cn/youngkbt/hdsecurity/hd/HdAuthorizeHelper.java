package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.authorize.AuthorizeType;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityAuthorizeException;
import cn.youngkbt.hdsecurity.strategy.HdSecurityElementVagueMatchStrategy;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;

import java.util.Collections;
import java.util.List;

/**
 * Hd Security Authorize 模块
 *
 * @author Tianke
 * @date 2024/12/12 23:12:40
 * @since 1.0.0
 */
public class HdAuthorizeHelper {

    private final String accountType;

    public HdAuthorizeHelper() {
        this(DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdAuthorizeHelper(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    // ---------- 角色相关操作方法 ----------

    public List<String> getRoleList() {
        return getAuthorizeList(AuthorizeType.ROLE);
    }

    public List<String> getRoleList(Object loginId) {
        return getAuthorizeList(AuthorizeType.ROLE, loginId);
    }

    public boolean hasRole(String role) {
        return hasAuthorize(AuthorizeType.ROLE, role);
    }

    public boolean hasRole(Object loginId, String role) {
        return hasAuthorize(AuthorizeType.ROLE, loginId, role);
    }

    public boolean hasRole(List<String> roleList, String role) {
        return hasAuthorize(AuthorizeType.ROLE, roleList, role);
    }

    public void checkRole(String role) {
        checkAuthorize(AuthorizeType.ROLE, role);
    }

    public boolean hasRoleAnd(String... role) {
        return hasAuthorizeAnd(AuthorizeType.ROLE, role);
    }

    public void checkRoleAnd(String... permissions) {
        checkAuthorizeAnd(AuthorizeType.ROLE, permissions);
    }

    public boolean hasRoleOr(String... permissions) {
        return hasAuthorizeOr(AuthorizeType.ROLE, permissions);
    }

    public void checkRoleOr(String... permissions) {
        checkAuthorizeOr(AuthorizeType.ROLE, permissions);
    }

    // ---------- 权限相关操作方法 ----------
    public List<String> getPermissionList() {
        return getAuthorizeList(AuthorizeType.PERMISSION);
    }

    public List<String> getPermissionList(Object loginId) {
        return getAuthorizeList(AuthorizeType.PERMISSION, loginId);
    }

    public boolean hasPermission(String permission) {
        return hasPermission(getPermissionList(), permission);
    }

    public boolean hasPermission(Object loginId, String permission) {
        return hasAuthorize(AuthorizeType.PERMISSION, loginId, permission);
    }

    public boolean hasPermission(List<String> permissionList, String permission) {
        return hasAuthorize(AuthorizeType.PERMISSION, permissionList, permission);
    }

    public void checkPermission(String permission) {
        checkAuthorize(AuthorizeType.PERMISSION, permission);
    }

    public boolean hasPermissionAnd(String... permissions) {
        return hasAuthorizeAnd(AuthorizeType.PERMISSION, permissions);
    }

    public void checkPermissionAnd(String... permissions) {
        checkAuthorizeAnd(AuthorizeType.PERMISSION, permissions);
    }

    public boolean hasPermissionOr(String... permissions) {
        return hasAuthorizeOr(AuthorizeType.PERMISSION, permissions);
    }

    public void checkPermissionOr(String... permissions) {
        checkAuthorizeOr(AuthorizeType.PERMISSION, permissions);
    }

    // ---------- 认证相关操作方法  ----------

    /**
     * 获取当前账号所拥有的指定认证类型下的所有认证码
     *
     * @param authorizeType 认证类型
     * @return 认证码列表
     */
    public List<String> getAuthorizeList(AuthorizeType authorizeType) {
        return getAuthorizeList(authorizeType, HdHelper.loginHelper(accountType).getLoginId());
    }

    /**
     * 获取当前账号所拥有的指定认证类型和指定账号类型的所有认证码
     *
     * @param authorizeType 认证类型
     * @param loginId       账号ID
     * @return 认证码列表
     */
    public List<String> getAuthorizeList(AuthorizeType authorizeType, Object loginId) {
        if (null == authorizeType) {
            return Collections.emptyList();
        }

        if (authorizeType.equals(AuthorizeType.ROLE)) {
            return HdSecurityManager.getAuthorize().getRoleList(loginId, accountType);
        }

        if (authorizeType.equals(AuthorizeType.PERMISSION)) {
            return HdSecurityManager.getAuthorize().getPermissionList(loginId, accountType);
        }

        return Collections.emptyList();
    }

    /**
     * 判断当前账号是否含有指定权限
     *
     * @param authorizeType 认证类型
     * @param authorize     认证码
     * @return 是否含有指定权限
     */
    public boolean hasAuthorize(AuthorizeType authorizeType, String authorize) {
        return hasAuthorize(authorizeType, HdHelper.loginHelper(accountType).getLoginId(), authorize);
    }

    /**
     * 判断当前账号是否含有指定权限
     *
     * @param authorizeType 认证类型
     * @param loginId       账号ID
     * @param authorize     认证码
     * @return 是否含有指定权限
     */
    public boolean hasAuthorize(AuthorizeType authorizeType, Object loginId, String authorize) {
        if (null == authorizeType) {
            return false;
        }

        List<String> authorizeList = getAuthorizeList(authorizeType, loginId);
        return hasAuthorize(authorizeList, authorize);
    }

    /**
     * 判断当前账号是否含有指定权限
     *
     * @param authorities 认证码列表
     * @param authority   认证码
     * @return 是否含有指定权限
     */
    public boolean hasAuthorize(List<String> authorities, String authority) {
        return HdSecurityElementVagueMatchStrategy.instance.vagueMatchElement.apply(authorities, authority);
    }

    /**
     * 校验当前账号是否含有指定认证码，如果验证失败，则抛出异常
     *
     * @param authorizeType 认证类型
     * @param authorize     认证码
     */
    public void checkAuthorize(AuthorizeType authorizeType, String authorize) {
        if (null == authorizeType) {
            return;
        }
        if (!hasAuthorize(authorizeType, authorize)) {
            throwAuthorizeException(authorizeType, authorize);
        }
    }

    /**
     * 判断当前账号是否含有指定认证码
     * <p>可指定多个认证码，必须全部拥有才返回 true</p>
     *
     * @param authorizeType 认证类型
     * @param authorizeList 认证码列表
     * @return 是否含有指定权限
     */
    public boolean hasAuthorizeAnd(AuthorizeType authorizeType, String... authorizeList) {
        try {
            checkAuthorizeAnd(authorizeType, authorizeList);
            return true;
        } catch (HdSecurityAuthorizeException e) {
            return false;
        }
    }

    /**
     * 校验当前账号是否含有指定认证码
     * <p>可指定多个认证码，全部验证通过则通过，否则则抛出异常</p>
     *
     * @param authorizeType 认证类型
     * @param authorizes    认证码列表
     */
    public void checkAuthorizeAnd(AuthorizeType authorizeType, String... authorizes) {
        if (null == authorizeType || HdCollectionUtil.isEmpty(authorizes)) {
            return;
        }

        // 下面两个方法里都获取了 loginId，因此这里先获取，提高性能
        Object loginId = HdHelper.loginHelper(accountType).getLoginId();
        List<String> authorizeList = getAuthorizeList(authorizeType, loginId);

        for (String authorize : authorizes) {
            if (!hasAuthorize(authorizeList, authorize)) {
                throwAuthorizeException(authorizeType, authorize);
            }
        }
    }

    /**
     * 判断当前账号是否含有指定认证码
     * <p>可指定多个认证码，任意一个认证码符合就返回 true</p>
     *
     * @param authorizeType 认证类型
     * @param authorizes    认证码列表
     * @return 是否含有指定权限
     */
    public boolean hasAuthorizeOr(AuthorizeType authorizeType, String... authorizes) {
        try {
            checkAuthorizeOr(authorizeType, authorizes);
            return true;
        } catch (HdSecurityAuthorizeException e) {
            return false;
        }
    }

    /**
     * 检查当前账号是否含有指定认证码
     * <p>可指定多个认证码，只要有一个验证通过则通过，否则抛出异常</p>
     *
     * @param authorizeType 认证类型
     * @param authorizes    认证码列表
     */
    public void checkAuthorizeOr(AuthorizeType authorizeType, String... authorizes) {
        if (null == authorizeType || HdCollectionUtil.isEmpty(authorizes)) {
            return;
        }

        // 下面两个方法里都获取了 loginId，因此这里先获取，提高性能
        Object loginId = HdHelper.loginHelper(accountType).getLoginId();
        List<String> authorizeList = getAuthorizeList(authorizeType, loginId);

        for (String authorize : authorizes) {
            if (hasAuthorize(authorizeList, authorize)) {
                return;
            }
        }
        throwAuthorizeException(authorizeType, String.join(", ", authorizes));
    }

    /**
     * 根据认证类型抛出对应的异常
     *
     * @param authorizeType 认证类型
     * @param authorize     认证码
     */
    public void throwAuthorizeException(AuthorizeType authorizeType, String authorize) {
        if (authorizeType.equals(AuthorizeType.ROLE)) {
            throw new HdSecurityAuthorizeException(authorize, "角色", accountType).setCode(HdSecurityErrorCode.AUTHORIZE_ROLE_INVALID);
        }

        if (authorizeType.equals(AuthorizeType.PERMISSION)) {
            throw new HdSecurityAuthorizeException(authorize, "权限", accountType).setCode(HdSecurityErrorCode.AUTHORIZE_PERMISSION_INVALID);
        }
    }
}
