package cn.youngkbt.hdsecurity.jwt.support.stateless;

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
 * JWT Stateless LoginHelper模块：无状态模式，JWT 不会缓存到持久层，一旦 JWT 丢失后将无法找回，并且该 JWT 无法注销，只能等待自身的过期时间到期
 *
 * @author Tianke
 * @date 2025/1/4 01:59:48
 * @since 1.0.0
 */
public class HdJwtLoginHelperForStateless extends HdLoginHelper {

    public HdJwtLoginHelperForStateless(String accountType) {
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
        throw new HdSecurityJwtException("JWT Stateless（无状态）模式下禁用登出相关操作：注销、替人下线、顶人下线");
    }
}
