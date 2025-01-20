package cn.youngkbt.hdsecurity.repository;

import cn.youngkbt.hdsecurity.GlobalEventEnums;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;

/**
 * Hd SSecurity 持久层实现类提供者
 *
 * @author Tianke
 * @date 2024/11/25 23:03:29
 * @since 1.0.0
 */
public class HdSecurityRepositoryProvider {

    private HdSecurityRepositoryProvider() {
    }

    private static HdSecurityRepository hdSecurityRepository = new HdSecurityRepositoryForMap();

    /**
     * 获取持久层实现类
     *
     * @return 持久层实现类
     */
    public static HdSecurityRepository getHdSecurityRepository() {
        return hdSecurityRepository;
    }

    /**
     * 设置持久层实现类
     *
     * @param hdSecurityRepository 持久层实现类
     */
    public static void setHdSecurityRepository(HdSecurityRepository hdSecurityRepository) {
        HdSecurityEventCenter.publishBeforeComponentRegister(GlobalEventEnums.REGISTER_REPOSITORY.getFunctionName(), hdSecurityRepository);
        if (null != HdSecurityRepositoryProvider.hdSecurityRepository) {
            hdSecurityRepository.destroy();
        }
        if (null != hdSecurityRepository) {
            hdSecurityRepository.init();
        }
        HdSecurityRepositoryProvider.hdSecurityRepository = hdSecurityRepository;
        // 发布组件注册事件
        HdSecurityEventCenter.publishAfterComponentRegister(GlobalEventEnums.REGISTER_REPOSITORY.getFunctionName(), hdSecurityRepository);
    }
}
