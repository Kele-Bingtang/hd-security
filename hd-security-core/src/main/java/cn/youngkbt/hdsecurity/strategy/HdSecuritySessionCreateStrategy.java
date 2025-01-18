package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.function.HdCreateSessionFunction;
import cn.youngkbt.hdsecurity.model.session.HdAccountSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenSession;

/**
 * HdSession 会话创建策略
 *
 * @author Tianke
 * @date 2024/11/30 18:09:54
 * @since 1.0.0
 */
public class HdSecuritySessionCreateStrategy {

    public static HdSecuritySessionCreateStrategy instance = new HdSecuritySessionCreateStrategy();
    // 创建 Account Session 的函数
    public HdCreateSessionFunction<HdAccountSession> createAccountSession = HdAccountSession::new;
    // 创建 Token Session 的函数
    public HdCreateSessionFunction<HdTokenSession> createTokenSession = HdTokenSession::new;
    
    public HdSecuritySessionCreateStrategy setCreateAccountSession(HdCreateSessionFunction createAccountSession) {
        this.createAccountSession = createAccountSession;
        return this;
    }

    public HdSecuritySessionCreateStrategy setCreateTokenSession(HdCreateSessionFunction createTokenSession) {
        this.createTokenSession = createTokenSession;
        return this;
    }
}
