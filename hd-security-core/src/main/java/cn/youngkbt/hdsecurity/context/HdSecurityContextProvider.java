package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.GlobalEventEnums;
import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;

/**
 * @author Tianke
 * @date 2024/11/30 15:26:10
 * @since 1.0.0
 */
public class HdSecurityContextProvider {

    private HdSecurityContextProvider() {
    }

    /**
     * 默认的上下文处理器为报错处理器
     */
    private static HdSecurityContext hdSecurityContext = new HdSecurityContextForDefault();

    public static HdSecurityContext getHdSecurityContext() {
        return hdSecurityContext;
    }

    public static void setHdSecurityContext(HdSecurityContext hdSecurityContext) {
        HdSecurityEventCenter.publishBeforeComponentRegister(GlobalEventEnums.REGISTER_CONTEXT.getFunctionName(), hdSecurityContext);
        HdSecurityContextProvider.hdSecurityContext = hdSecurityContext;
        HdSecurityEventCenter.publishAfterComponentRegister(GlobalEventEnums.REGISTER_CONTEXT.getFunctionName(), hdSecurityContext);
    }

    public static HdSecurityRequest getRequest() {
        return hdSecurityContext.getRequest();
    }

    public static HdSecurityResponse getResponse() {
        return hdSecurityContext.getResponse();
    }

    public static HdSecurityStorage getStorage() {
        return hdSecurityContext.getStorage();
    }

    public static HdSecurityApplication getApplication() {
        return HdSecurityApplication.instance;
    }
}
