package cn.youngkbt.hdsecurity.demo.core;

import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.listener.impl.HdSecurityEventListenerForLog;

/**
 * @author Tianke
 * @date 2024/11/25 01:10:49
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        HdSecurityConfigProvider.getHdSecurityConfig();
        System.out.println(HdSecurityEventListenerForLog.getStartCostTimeMap());

        HdHelper.loginHelper().login("1001");


        {
            
        }

    }

}

