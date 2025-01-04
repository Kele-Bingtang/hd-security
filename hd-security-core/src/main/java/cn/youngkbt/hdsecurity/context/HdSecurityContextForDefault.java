package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityContextException;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

/**
 * Hd Security 全局上下文默认实现类，默认不支持 Web 环境，抛出异常，需要引用 Hd Security Web 相关依赖
 *
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

    @Override
    public boolean matchPath(String pattern, String path) {
        return HdStringUtil.vagueMatch(pattern, path);
    }
}
