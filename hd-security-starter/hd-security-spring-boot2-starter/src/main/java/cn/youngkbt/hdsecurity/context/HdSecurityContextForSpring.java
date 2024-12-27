package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.HdSecurityRequestForServlet;
import cn.youngkbt.hdsecurity.HdSecurityResponseForServlet;
import cn.youngkbt.hdsecurity.HdSecurityStorageForServlet;
import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.utils.PathMatcherHolder;
import cn.youngkbt.hdsecurity.utils.SpringMVCUtil;

/**
 * @author Tianke
 * @date 2024/12/24 00:40:54
 * @since 1.0.0
 */
public class HdSecurityContextForSpring implements HdSecurityContext {
    @Override
    public HdSecurityRequest getRequest() {
        return new HdSecurityRequestForServlet(SpringMVCUtil.getRequest());
    }

    @Override
    public HdSecurityResponse getResponse() {
        return new HdSecurityResponseForServlet(SpringMVCUtil.getResponse());
    }

    @Override
    public HdSecurityStorage getStorage() {
        return new HdSecurityStorageForServlet(SpringMVCUtil.getRequest());
    }

    @Override
    public boolean matchPath(String pattern, String path) {
        return PathMatcherHolder.match(pattern, path);
    }
}