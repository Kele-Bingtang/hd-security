package cn.youngkbt.hdsecurity.model.login;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;

import java.util.Map;

/**
 * Hd Security 登录模型
 *
 * @author Tianke
 * @date 2024/11/25 01:14:35
 * @since 1.0.0
 */
public class HdLoginModel {
    /**
     * 登录 ID
     */
    private Object loginId;
    /**
     * 账号类型
     */
    private String accountType = DefaultConstant.DEFAULT_ACCOUNT_TYPE;
    /**
     * 设备名称
     */
    private String device = DefaultConstant.DEFAULT_LOGIN_DEVICE;
    /**
     * 是否记住我（记住我即创建持久 Cookie：临时 Cookie 在浏览器关闭时会自动删除，持久 Cookie 在重新打开后依然存在）
     */
    private Boolean rememberMe = true;
    /**
     * Token 过期时间（单位：秒）,如未指定，则使用全局配置的 timeout 值
     */
    private Long tokenExpireTime;

    /**
     * Token 最低活跃频率，单位：秒（如未指定，则使用全局配置的 activeTimeout 值）
     */
    private Long tokenActiveExpireTime;

    /**
     * 自定义 Token（自定义本次登录生成的 Token 值）
     */
    private String token;

    /**
     * 是否在登录后将 Token 写入到响应头
     */
    private Boolean isWriteHeader;

    /**
     * 本次登录挂载到 HdDevice 的数据
     */
    private Map<String, Object> tokenDeviceData;

    /**
     * 扩展信息（引入 hd-security-jwt 依赖后生效）
     */
    private Map<String, Object> extraData;

    public Object getLoginId() {
        return loginId;
    }

    public HdLoginModel setLoginId(Object loginId) {
        this.loginId = loginId;
        return this;
    }

    public String getAccountType() {
        return accountType;
    }

    public HdLoginModel setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getDevice() {
        return device;
    }

    public HdLoginModel setDevice(String device) {
        this.device = device;
        return this;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public HdLoginModel setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
        return this;
    }

    public Long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public HdLoginModel setTokenExpireTime(Long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
        return this;
    }

    public Long getTokenActiveExpireTime() {
        return tokenActiveExpireTime;
    }

    public HdLoginModel setTokenActiveExpireTime(Long tokenActiveExpireTime) {
        this.tokenActiveExpireTime = tokenActiveExpireTime;
        return this;
    }

    public String getToken() {
        return token;
    }

    public HdLoginModel setToken(String token) {
        this.token = token;
        return this;
    }

    public Boolean getWriteHeader() {
        return isWriteHeader;
    }

    public HdLoginModel setWriteHeader(Boolean writeHeader) {
        isWriteHeader = writeHeader;
        return this;
    }

    public Map<String, Object> getTokenDeviceData() {
        return tokenDeviceData;
    }

    public HdLoginModel setTokenDeviceData(Map<String, Object> tokenDeviceData) {
        this.tokenDeviceData = tokenDeviceData;
        return this;
    }

    public int getCookieExpireTime() {
        if (!Boolean.TRUE.equals(rememberMe)) {
            return -1;
        }

        if (null == tokenExpireTime) {
            long expireTime = HdSecurityManager.getConfig(accountType).getTokenExpireTime();
            if (-1 == expireTime || expireTime > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            return Math.toIntExact(expireTime);
        }

        if (tokenExpireTime > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return Math.toIntExact(tokenExpireTime);
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public HdLoginModel setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
        return this;
    }

    @Override
    public String toString() {
        return "HdLoginModel{" +
                "loginId=" + loginId +
                ", accountType='" + accountType + '\'' +
                ", device='" + device + '\'' +
                ", rememberMe=" + rememberMe +
                ", tokenExpireTime=" + tokenExpireTime +
                ", tokenActiveExpireTime=" + tokenActiveExpireTime +
                ", token='" + token + '\'' +
                ", isWriteHeader=" + isWriteHeader +
                ", tokenDeviceData=" + tokenDeviceData +
                ", extraData=" + extraData +
                '}';
    }
}
