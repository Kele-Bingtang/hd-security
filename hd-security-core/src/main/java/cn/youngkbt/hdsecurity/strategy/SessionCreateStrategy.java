package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.function.HdCreateSessionFunction;
import cn.youngkbt.hdsecurity.model.session.HdTokenSession;

/**
 * HdSession 会话创建策略
 *
 * @author Tianke
 * @date 2024/11/30 18:09:54
 * @since 1.0.0
 */
public class SessionCreateStrategy {

    public static SessionCreateStrategy instance = new SessionCreateStrategy();

    public HdCreateSessionFunction createSession = HdTokenSession::new;

    public SessionCreateStrategy setCreateSession(HdCreateSessionFunction createSession) {
        this.createSession = createSession;
        return this;
    }
}
