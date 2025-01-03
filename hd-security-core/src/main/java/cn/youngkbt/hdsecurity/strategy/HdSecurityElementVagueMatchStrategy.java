package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.function.HdVagueMatchElementFunction;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;

/**
 * 集合中是否包含指定元素（模糊匹配，支持 * 匹配）策略类
 *
 * @author Tianke
 * @date 2024/11/30 18:33:12
 * @since 1.0.0
 */
public class HdSecurityElementVagueMatchStrategy {

    public static HdSecurityElementVagueMatchStrategy instance = new HdSecurityElementVagueMatchStrategy();

    public HdVagueMatchElementFunction vagueMatchElement = HdCollectionUtil::vagueMatchElement;

    public HdSecurityElementVagueMatchStrategy setVagueMatchElement(HdVagueMatchElementFunction vagueMatchElement) {
        this.vagueMatchElement = vagueMatchElement;
        return this;
    }
}
