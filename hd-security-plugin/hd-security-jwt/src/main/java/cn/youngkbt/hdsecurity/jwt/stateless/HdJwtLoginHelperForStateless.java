package cn.youngkbt.hdsecurity.jwt.stateless;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.hd.HdLoginHelper;
import cn.youngkbt.hdsecurity.model.cookie.HdCookieOperator;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

/**
 * JWT Stateless LoginHelper 模块：无状态模式，JWT 不会缓存到持久层，一旦 JWT 丢失后将无法找回，并且该 JWT 无法注销，只能等待自身的过期时间到期
 *
 * @author Tianke
 * @date 2025/1/4 01:59:48
 * @since 1.0.0
 */
public class HdJwtLoginHelperForStateless extends HdLoginHelper {

    public HdJwtLoginHelperForStateless(String accountType) {
        super(accountType);
    }

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
}
