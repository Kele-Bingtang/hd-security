package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.function.helper.HdLoginHelperCreateFunction;
import cn.youngkbt.hdsecurity.function.helper.HdSessionHelperCreateFunction;
import cn.youngkbt.hdsecurity.function.helper.HdTokenHelperCreateFunction;
import cn.youngkbt.hdsecurity.hd.HdLoginHelper;
import cn.youngkbt.hdsecurity.hd.HdSessionHelper;
import cn.youngkbt.hdsecurity.hd.HdTokenHelper;

/**
 * Helper 创建策略
 *
 * @author Tianke
 * @date 2024/11/30 18:14:51
 * @since 1.0.0
 */
public class HelperCreateStrategy {

    public static HelperCreateStrategy instance = new HelperCreateStrategy();

    public HdLoginHelperCreateFunction createLoginHelper = HdLoginHelper::new;
    public HdSessionHelperCreateFunction createSessionHelper = HdSessionHelper::new;
    public HdTokenHelperCreateFunction createTokenHelper = HdTokenHelper::new;

    public HelperCreateStrategy setCreateLoginHelper(HdLoginHelperCreateFunction createLoginHelper) {
        this.createLoginHelper = createLoginHelper;
        return this;
    }

    public HelperCreateStrategy setCreateSessionHelper(HdSessionHelperCreateFunction createSessionHelper) {
        this.createSessionHelper = createSessionHelper;
        return this;
    }

    public HelperCreateStrategy setCreateTokenHelper(HdTokenHelperCreateFunction createTokenHelper) {
        this.createTokenHelper = createTokenHelper;
        return this;
    }
}
