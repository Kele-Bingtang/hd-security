package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;

/**
 * @author Tianke
 * @date 2024/11/30 15:25:44
 * @since 1.0.0
 */
public interface HdSecurityContext {
    /**
     * 获取当前请求的 Request 包装对象
     * @return Request 包装对象
     */
    HdSecurityRequest getRequest();

    /**
     * 获取当前请求的 Response 包装对象
     * @return Response 包装对象
     */
    HdSecurityResponse getResponse();

    /**
     * 获取当前请求的 Storage 包装对象
     * @return Storage 包装对象
     */
    HdSecurityStorage getStorage();
}
