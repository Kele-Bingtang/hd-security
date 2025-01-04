package cn.youngkbt.hdsecurity.log;

import cn.youngkbt.hdsecurity.GlobalEventEnums;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;

/**
 * Hd Security 日志实现类提供者
 *
 * @author Tianke
 * @date 2024/11/25 23:01:20
 * @since 1.0.0
 */
public class HdSecurityLogProvider {

    private HdSecurityLogProvider() {
    }

    public static HdSecurityLog log = new HdSecurityLogForConsole();

    /**
     * 获取日志实现类
     *
     * @return 日志实现类
     */
    public static HdSecurityLog getLog() {
        return log;
    }

    /**
     * 设置日志实现类
     *
     * @param hdSecurityLog 日志实现类
     */
    public static void setLog(HdSecurityLog hdSecurityLog) {
        HdSecurityEventCenter.publishBeforeComponentRegister(GlobalEventEnums.REGISTER_LOG.getFunctionName(), hdSecurityLog);
        hdSecurityLog.init();
        HdSecurityLogProvider.log = hdSecurityLog;
        // 发布组件注册事件
        HdSecurityEventCenter.publishAfterComponentRegister(GlobalEventEnums.REGISTER_LOG.getFunctionName(), hdSecurityLog);
    }

}
