package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.utils.HdSecurityReactorHolder;
import cn.youngkbt.hdsecurity.utils.PathMatcherHolder;

/**
 * Hd Security 上下文实现类，基于 Reactor SpringBoot3 实现
 *
 * @author Tianke
 * @date 2025/1/1 16:51:39
 * @since 1.0.0
 */
public class HdSecurityContextForSpringReactor implements HdSecurityContext {

    @Override
    public HdSecurityRequest getRequest() {
        return HdSecurityReactorHolder.getRequest();
    }

    @Override
    public HdSecurityResponse getResponse() {
        return HdSecurityReactorHolder.getResponse();
    }

    @Override
    public HdSecurityStorage getStorage() {
        return HdSecurityReactorHolder.getStorage();
    }

    @Override
    public boolean matchPath(String pattern, String path) {
        return PathMatcherHolder.match(pattern, path);
    }
}
