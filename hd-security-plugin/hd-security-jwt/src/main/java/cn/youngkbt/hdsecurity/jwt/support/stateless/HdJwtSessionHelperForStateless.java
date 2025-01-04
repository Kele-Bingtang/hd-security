package cn.youngkbt.hdsecurity.jwt.support.stateless;

import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.hd.HdSessionHelper;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;

/**
 * JWT Stateless SessionHelper 模块：无状态模式，JWT 不会缓存到持久层，一旦 JWT 丢失后将无法找回，并且该 JWT 无法注销，只能等待自身的过期时间到期
 *
 * @author Tianke
 * @date 2025/1/4 01:23:57
 * @since 1.0.0
 */
public class HdJwtSessionHelperForStateless extends HdSessionHelper {

    public HdJwtSessionHelperForStateless(String accountType) {
        super(accountType);
    }

    /**
     * 创建账号会话
     *
     * @param hdLoginModel 登录模型
     * @return token
     */
    @Override
    public String createAccountSession(HdLoginModel hdLoginModel) {
        // 检查登录模型
        HdHelper.loginHelper(getAccountType()).checkLoginModel(hdLoginModel);
        // 初始化登录模型
        HdLoginModel loginModel = HdLoginModelOperator.mutate(hdLoginModel);

        return HdHelper.tokenHelper(getAccountType()).createToken(loginModel);
    }
}
