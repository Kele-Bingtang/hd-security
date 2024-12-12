package cn.youngkbt.hdsecurity.demo.core;

import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.listener.impl.HdSecurityEventListenerForLog;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;

import java.util.Arrays;

/**
 * @author Tianke
 * @date 2024/11/25 01:10:49
 * @since 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        HdSecurityConfigProvider.getHdSecurityConfig();
        System.out.println("Hd Security 启动耗费时长明细(s)：" + HdSecurityEventListenerForLog.getStartCostTimeMap());

        String accountType = "Test";
        HdHelper.login("1001", HdLoginModelOperator.build().setAccountType(accountType));

        System.out.println(HdHelper.loginHelper(accountType).isLogin("1001"));

        System.out.println(HdCollectionUtil.vagueMatchElement(Arrays.asList("user*", "user:edit", "user:remove"), "user:add"));
    }
}

