package cn.youngkbt.hdsecurity.jwt.support.cache;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.exception.HdSecurityJwtException;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.hd.HdLoginHelper;
import cn.youngkbt.hdsecurity.model.cookie.HdCookieOperator;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

/**
 * JWT Cache LoginHelper 模块：缓存模式，JWT 存入持久层，仅用于判断是否过期。不会缓存与 LoginId 相关的信息，因此无法执行登出相关操作：注销、替人下线、顶人下线
 *
 * @author Tianke
 * @date 2025/1/4 02:18:39
 * @since 1.0.0
 */
public class HdJwtLoginHelperForCache extends HdLoginHelper {

    public HdJwtLoginHelperForCache(String accountType) {
        super(accountType);
    }

    /**
     * 注销
     */
    @Override
    public void logout() {
        String webToken = HdHelper.tokenHelper(getAccountType()).getWebToken();
        if (HdStringUtil.hasEmpty(webToken)) {
            return;
        }

        HdSecurityConfig config = HdSecurityManager.getConfig(getAccountType());
        // 如果「尝试从 cookie 里读取 token」功能，则清除 Cookie
        if (Boolean.TRUE.equals(config.getReadCookie())) {
            HdCookieOperator.removeCookie(config.getSecurityPrefixKey(), config.getCookie());
        }

        // 从作用域里清除 Token，因为在登录成功后会往作用域里存入 Token，具体请看 HdTokenHelper#writeTokenToStorage() 方法
        HdSecurityStorage storage = HdSecurityManager.getContext().getStorage();
        if (null != storage) {
            storage.remove(DefaultConstant.CREATED_TOKEN);
            storage.remove(DefaultConstant.CREATED_TOKEN_PREFIX);
        }
    }

    /**
     * 退出登录，如果账号会话为空，则根据 token 获取对应的账号会话
     * <p>注销、踢人下线、顶人下线都用到该方法</p>
     *
     * @param token             Token
     * @param accountSession    账号会话
     * @param exitExtraRunnable 退出登录的额外逻辑，给注销、踢人下线、顶人下线分别传入对应的逻辑
     */
    @Override
    public void exitLoginByToken(String token, HdSession accountSession, Runnable exitExtraRunnable) {
        throw new HdSecurityJwtException("JWT Cache 模式下禁用登出相关操作：注销、替人下线、顶人下线");
    }
}
