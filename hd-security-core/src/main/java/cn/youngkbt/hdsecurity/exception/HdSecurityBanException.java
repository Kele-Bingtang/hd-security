package cn.youngkbt.hdsecurity.exception;

/**
 * @author Tianke
 * @date 2024/12/13 00:47:18
 * @since 1.0.0
 */
public class HdSecurityBanException extends HdSecurityException {
    /**
     * 账号类型
     */
    private String accountType;

    /**
     * 被封禁的账号id
     */
    private Object loginId;

    /**
     * 具体被封禁的服务
     */
    private String realm;

    /**
     * 具体被封禁的等级
     */
    private int level;

    /**
     * 校验时要求低于的等级
     */
    private int limitLevel;

    /**
     * 封禁剩余时间，单位：秒
     */
    private long disableTime;

    public String getAccountType() {
        return accountType;
    }

    public Object getLoginId() {
        return loginId;
    }

    public String getRealm() {
        return realm;
    }

    public int getLevel() {
        return level;
    }

    public int getLimitLevel() {
        return limitLevel;
    }

    public long getDisableTime() {
        return disableTime;
    }

    public HdSecurityBanException(String message) {
        super(message);
    }

    public HdSecurityBanException(String accountType, Object loginId, String realm, int level, int limitLevel, long disableTime) {
        super(String.format("账号 [%s] 已封禁，封禁服务：[%s]，封禁等级：[%d]，封禁时间：[%d] 秒，当前校验封禁等级：[%d]", loginId, realm, level, disableTime, limitLevel));
        this.accountType = accountType;
        this.loginId = loginId;
        this.realm = realm;
        this.level = level;
        this.limitLevel = limitLevel;
        this.disableTime = disableTime;
    }
}
