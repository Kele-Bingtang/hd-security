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

    public static final HdSecurityHelperCreateStrategy instance = new HdSecurityHelperCreateStrategy();

    private HdLoginHelperCreateFunction createLoginHelper = HdLoginHelper::new;
    private HdSessionHelperCreateFunction createSessionHelper = HdSessionHelper::new;
    private HdTokenHelperCreateFunction createTokenHelper = HdTokenHelper::new;
    private HdBanAccountHelperCreateFunction createBanAccountHelper = HdBanAccountHelper::new;
    private HdAuthorizeHelperCreateFunction createAuthorizeHelper = HdAuthorizeHelper::new;
    private HdAnnotationHelperCreateSupplier createAnnotationHelper = HdAnnotationHelper::new;
    private HdBasicAuthHelperCreateSupplier createBasicAuthHelper = HdBasicAuthHelper::new;
    private HdSameOriginTokenHelperCreateSupplier createSameOriginTokenHelper = HdSameOriginTokenHelper::new;

    public HdLoginHelperCreateFunction getCreateLoginHelper() {
        return createLoginHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateLoginHelper(HdLoginHelperCreateFunction createLoginHelper) {
        this.createLoginHelper = createLoginHelper;
        return this;
    }

    public HdSessionHelperCreateFunction getCreateSessionHelper() {
        return createSessionHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateSessionHelper(HdSessionHelperCreateFunction createSessionHelper) {
        this.createSessionHelper = createSessionHelper;
        return this;
    }

    public HdTokenHelperCreateFunction getCreateTokenHelper() {
        return createTokenHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateTokenHelper(HdTokenHelperCreateFunction createTokenHelper) {
        this.createTokenHelper = createTokenHelper;
        return this;
    }

    public HdBanAccountHelperCreateFunction getCreateBanAccountHelper() {
        return createBanAccountHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateBanAccountHelper(HdBanAccountHelperCreateFunction createBanAccountHelper) {
        this.createBanAccountHelper = createBanAccountHelper;
        return this;
    }

    public HdAuthorizeHelperCreateFunction getCreateAuthorizeHelper() {
        return createAuthorizeHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateAuthorizeHelper(HdAuthorizeHelperCreateFunction createAuthorizeHelper) {
        this.createAuthorizeHelper = createAuthorizeHelper;
        return this;
    }

    public HdAnnotationHelperCreateSupplier getCreateAnnotationHelper() {
        return createAnnotationHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateAnnotationHelper(HdAnnotationHelperCreateSupplier createAnnotationHelper) {
        this.createAnnotationHelper = createAnnotationHelper;
        return this;
    }

    public HdBasicAuthHelperCreateSupplier getCreateBasicAuthHelper() {
        return createBasicAuthHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateBasicAuthHelper(HdBasicAuthHelperCreateSupplier createBasicAuthHelper) {
        this.createBasicAuthHelper = createBasicAuthHelper;
        return this;
    }

    public HdSameOriginTokenHelperCreateSupplier getCreateSameOriginTokenHelper() {
        return createSameOriginTokenHelper;
    }

    public HdSecurityHelperCreateStrategy setCreateSameOriginTokenHelper(HdSameOriginTokenHelperCreateSupplier createSameOriginTokenHelper) {
        this.createSameOriginTokenHelper = createSameOriginTokenHelper;
        return this;
    }
}
