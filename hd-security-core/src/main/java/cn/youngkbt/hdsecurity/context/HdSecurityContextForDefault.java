package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityContextException;

/**
 * @author Tianke
 * @date 2024/12/2 23:03:21
 * @since 1.0.0
 */
public class HdSecurityContextForDefault implements HdSecurityContext {
    /**
     * 错误提示语
     */
    public static final String ERROR_MESSAGE = "Hd Security 当前处于非 Web 环境，未能获取有效的上下文处理器";

    @Override
    public HdSecurityRequest getRequest() {
        throw new HdSecurityContextException(ERROR_MESSAGE).setCode(HdSecurityErrorCode.CONTEXT_GET_NULL);
    }

    @Override
    public HdSecurityResponse getResponse() {
        throw new HdSecurityContextException(ERROR_MESSAGE).setCode(HdSecurityErrorCode.CONTEXT_GET_NULL);
    }

    @Override
    public HdSecurityStorage getStorage() {
        throw new HdSecurityContextException(ERROR_MESSAGE).setCode(HdSecurityErrorCode.CONTEXT_GET_NULL);
    }
}
