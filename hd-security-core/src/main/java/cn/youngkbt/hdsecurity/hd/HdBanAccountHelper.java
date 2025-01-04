package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityBanException;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import static cn.youngkbt.hdsecurity.hd.RepositoryKeyHelper.getDisableAccountKey;

/**
 * Hd Security 封禁账号模块
 *
 * @author Tianke
 * @date 2024/12/13 00:40:28
 * @since 1.0.0
 */
public class HdBanAccountHelper {

    private final String accountType;

    public HdBanAccountHelper() {
        this(DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdBanAccountHelper(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    /**
     * 获取封禁账号的封禁级别，如果尚未被封禁，返回 null
     *
     * @param loginId 账号 ID
     * @return 封禁级别，如果尚未被封禁，返回 null
     */
    public Integer getDisableLevel(Object loginId) {
        return getDisableLevel(loginId, DefaultConstant.DEFAULT_BAN_REALM);
    }

    /**
     * 获取封禁账号指定领域的封禁级别，如果尚未被封禁，返回 null
     *
     * @param loginId 账号 ID
     * @param realm   领域
     * @return 封禁级别，如果尚未被封禁，返回 null
     */
    public Integer getDisableLevel(Object loginId, String realm) {
        Object level = HdSecurityManager.getRepository().query(getDisableAccountKey(accountType, loginId, realm));
        // 判断是否被封禁了，如果尚未被封禁，返回 -2
        if (HdStringUtil.hasEmpty(level)) {
            return null;
        }

        // 转为 int 类型
        return Integer.parseInt(String.valueOf(level));
    }

    /**
     * 获取封禁账号的封禁时间（单位：秒）
     * <p>如果返回 -1 代表永久封禁，返回 -2 代表未被封禁</p>
     *
     * @param loginId 账号 ID
     * @return 封禁时间，如果返回 -1 代表永久封禁，返回 -2 代表未被封禁
     */
    public long getDisabledTime(Object loginId) {
        return getDisabledTime(loginId, DefaultConstant.DEFAULT_BAN_REALM);
    }

    /**
     * 获取封禁账号指定领域的封禁时间（单位：秒）
     * <p>如果返回 -1 代表永久封禁，返回 -2 代表未被封禁</p>
     *
     * @param loginId 账号 ID
     * @param realm   领域
     * @return 封禁时间，如果返回 -1 代表永久封禁，返回 -2 代表未被封禁
     */
    public long getDisabledTime(Object loginId, String realm) {
        return HdSecurityManager.getRepository().getExpireTime(getDisableAccountKey(accountType, loginId, realm));
    }

    /**
     * 封禁账号
     *
     * @param loginId     账号 ID
     * @param disableTime 封禁时间（单位：秒）
     */
    public void disable(Object loginId, long disableTime) {
        disable(loginId, disableTime, DefaultConstant.DEFAULT_BAN_REALM, DefaultConstant.DEFAULT_BAN_LEVEL);
    }

    /**
     * 封禁账号的指定领域
     *
     * @param loginId     账号 ID
     * @param disableTime 封禁时间（单位：秒）
     * @param realm       领域
     */
    public void disable(Object loginId, long disableTime, String realm) {
        disable(loginId, disableTime, realm, DefaultConstant.DEFAULT_BAN_LEVEL);
    }

    /**
     * 封禁账号的指定领域，并且指定封禁级别
     *
     * @param loginId     账号 ID
     * @param disableTime 封禁时间（单位：秒）
     * @param realm       领域
     * @param level       封禁级别
     */
    public void disable(Object loginId, long disableTime, String realm, int level) {
        if (HdStringUtil.hasEmpty(loginId)) {
            throw new HdSecurityBanException("封禁的账号不能为空").setCode(HdSecurityErrorCode.BAN_ACCOUNT_INVALID);
        }

        if (HdStringUtil.hasEmpty(realm)) {
            throw new HdSecurityBanException("封禁的领域不能为空").setCode(HdSecurityErrorCode.BAN_REALM_INVALID);
        }

        if (level < DefaultConstant.MIN_BAN_LIMIT_LEVEL) {
            throw new HdSecurityBanException("封禁级别不能小于 " + DefaultConstant.MIN_BAN_LIMIT_LEVEL).setCode(HdSecurityErrorCode.BAN_LEVEL_INVALID);
        }

        // 发布封禁账号前置事件
        HdSecurityEventCenter.publishBeforeBanAccount(accountType, loginId, disableTime, realm, level);

        // 打上封禁标记
        HdSecurityManager.getRepository().add(getDisableAccountKey(accountType, loginId, realm), level, disableTime);

        // 发布封禁账号后置事件
        HdSecurityEventCenter.publishAfterBanAccount(accountType, loginId, disableTime, realm, level);
    }

    /**
     * 解封账号
     *
     * @param loginId 账号 ID
     */
    public void unDisable(Object loginId) {
        unDisable(loginId, DefaultConstant.DEFAULT_BAN_REALM);
    }

    /**
     * 解封账号的指定领域（可以支持多个领域）
     *
     * @param loginId 账号 ID
     * @param realms  领域
     */
    public void unDisable(Object loginId, String... realms) {
        if (HdStringUtil.hasEmpty(loginId)) {
            throw new HdSecurityBanException("封禁的账号不能为空").setCode(HdSecurityErrorCode.BAN_ACCOUNT_INVALID);
        }

        if (HdStringUtil.hasEmpty(realms)) {
            throw new HdSecurityBanException("封禁的领域不能为空").setCode(HdSecurityErrorCode.BAN_REALM_INVALID);
        }

        for (String realm : realms) {
            // 发布解封账号前置事件
            HdSecurityEventCenter.publishBeforeUnBanAccount(accountType, loginId, realm);
            
            // 解除账号封禁
            HdSecurityManager.getRepository().remove(getDisableAccountKey(accountType, loginId, realm));

            // 发布解封账号后置事件
            HdSecurityEventCenter.publishAfterUnBanAccount(accountType, loginId, realm);
        }
    }

    /**
     * 判断账号是否被封禁
     *
     * @param loginId 账号 ID
     * @return 是否被封禁
     */
    public boolean isDisable(Object loginId) {
        return isDisable(loginId, DefaultConstant.DEFAULT_BAN_REALM);
    }

    /**
     * 判断账号是否在指定领域被封禁
     *
     * @param loginId 账号 ID
     * @param realm   领域
     * @return 是否被封禁
     */
    public boolean isDisable(Object loginId, String realm) {
        return isDisable(loginId, realm, DefaultConstant.DEFAULT_BAN_LEVEL);
    }

    /**
     * 判断账号是否在指定领域被封禁，并且是否在指定的封禁级别内
     *
     * @param loginId 账号 ID
     * @param realm   领域
     * @param level   封禁级别
     * @return 是否被封禁
     */
    public boolean isDisable(Object loginId, String realm, int level) {
        int disableLevel = getDisableLevel(loginId, realm);
        
        if (disableLevel == DefaultConstant.NOT_BAN_TAG) {
            return false;
        }
        // 判断封禁等级是否达到了指定级别
        return disableLevel >= level;
    }

    /**
     * 检查账号是否被封禁
     *
     * @param loginId 账号 ID
     */
    public void checkDisable(Object loginId) {
        checkDisable(loginId, DefaultConstant.DEFAULT_BAN_REALM);
    }

    /**
     * 检查账号是否在指定领域被封禁
     *
     * @param loginId 账号 ID
     * @param realm   领域
     */
    public void checkDisable(Object loginId, String realm) {
        checkDisable(loginId, realm, DefaultConstant.DEFAULT_BAN_LEVEL);
    }

    /**
     * 检查账号是否在指定领域被封禁，并且是否在指定的封禁级别内，如果在，则抛出异常
     *
     * @param loginId 账号 ID
     * @param realm   领域
     * @param level   封禁级别
     */
    public void checkDisable(Object loginId, String realm, int level) {
        int disableLevel = getDisableLevel(loginId, realm);

        if (disableLevel == DefaultConstant.NOT_BAN_TAG) {
            return;
        }
        // 判断封禁等级是否达到了指定级别，到达了则抛出异常
        if (disableLevel >= level) {
            throw new HdSecurityBanException(accountType, loginId, realm, disableLevel, level, getDisabledTime(loginId, realm)).setCode(HdSecurityErrorCode.BAN_NOT_PASS);
        }
    }
}
