package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.function.helper.*;
import cn.youngkbt.hdsecurity.hd.*;

/**
 * Helper 创建策略
 *
 * @author Tianke
 * @date 2024/11/30 18:14:51
 * @since 1.0.0
 */
public class HdSecurityHelperCreateStrategy {

    public static HdSecurityHelperCreateStrategy instance = new HdSecurityHelperCreateStrategy();

    public HdLoginHelperCreateFunction createLoginHelper = HdLoginHelper::new;
    public HdSessionHelperCreateFunction createSessionHelper = HdSessionHelper::new;
    public HdTokenHelperCreateFunction createTokenHelper = HdTokenHelper::new;
    public HdBanAccountHelperCreateFunction createBanAccountHelper = HdBanAccountHelper::new;
    public HdAuthorizeHelperCreateFunction createAuthorizeHelper = HdAuthorizeHelper::new;

    public HdSecurityHelperCreateStrategy setCreateLoginHelper(HdLoginHelperCreateFunction createLoginHelper) {
        this.createLoginHelper = createLoginHelper;
        return this;
    }

    public HdSecurityHelperCreateStrategy setCreateSessionHelper(HdSessionHelperCreateFunction createSessionHelper) {
        this.createSessionHelper = createSessionHelper;
        return this;
    }

    public HdSecurityHelperCreateStrategy setCreateTokenHelper(HdTokenHelperCreateFunction createTokenHelper) {
        this.createTokenHelper = createTokenHelper;
        return this;
    }

    public HdSecurityHelperCreateStrategy setCreateBanAccountHelper(HdBanAccountHelperCreateFunction createBanAccountHelper) {
        this.createBanAccountHelper = createBanAccountHelper;
        return this;
    }

    public HdSecurityHelperCreateStrategy setCreateAuthorizeHelper(HdAuthorizeHelperCreateFunction createAuthorizeHelper) {
        this.createAuthorizeHelper = createAuthorizeHelper;
        return this;
    }
}
