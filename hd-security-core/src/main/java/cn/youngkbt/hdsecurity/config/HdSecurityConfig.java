package cn.youngkbt.hdsecurity.config;

import java.io.Serial;
import java.io.Serializable;

/**
 * Hd Security 配置类
 *
 * @author Tianke
 * @date 2024/11/25 00:51:23
 * @since 1.0.0
 */
public class HdSecurityConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * token 名称、存储 token 到 Cookie 的 key、前端提交 token 时参数的名称、存储 token 的 key 前缀
     */
    private String securityPrefixKey = "hd-security";

    /**
     * token 过期时间（单位：秒），-1 代表永久有效
     */
    private long tokenExpireTime = 60 * 60 * 24 * 30;

    /**
     * token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认 -1 代表不限制，永不冻结
     */
    private long tokenActiveExpireTime = -1;

    /**
     * 是否启用动态 activeTimeout 功能，如不需要请设置为 false，节省持久层空间（开启后将会将 tokenActiveExpireTime 相关信息存入到持久层）
     */
    private Boolean dynamicActiveExpireTime = false;

    /**
     * 是否允许同一账号多地同时登录 （为 true 时允许一起登录, 为 false 时新登录挤掉旧登录）
     */
    private Boolean isConcurrent = true;

    /**
     * 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token, 为 false 时每次登录新建一个 token）
     */
    private Boolean isShare = true;

    /**
     * 同一账号最大登录数量，-1代表不限 （只有在 isConcurrent=true, isShare=false 时此配置项才有意义）
     */
    private int maxLoginCount = 12;

    /**
     * 在每次创建 token 时的最高循环次数，用于保证 token 唯一性（-1=不循环尝试，直接使用）
     */
    private int maxTryTimes = 12;

    /**
     * 是否尝试从请求体里读取 token
     */
    private Boolean isReadBody = true;

    /**
     * 是否尝试从 header 里读取 token
     */
    private Boolean isReadHeader = true;

    /**
     * 是否尝试从 cookie 里读取 token
     */
    private Boolean isReadCookie = true;

    /**
     * 是否在登录后将 token 写入到响应头
     */
    private Boolean isWriteHeader = false;

    /**
     * token 风格（默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik）
     */
    private String tokenStyle = "uuid";

    /**
     * 默认 SaTokenDao 实现类中，每次清理过期数据间隔的时间（单位: 秒），默认值30秒，设置为 -1 代表不启动定时清理
     */
    private int dataRefreshPeriod = 30;

    /**
     * 获取 Token-Session 时是否必须登录（如果配置为true，会在每次获取 getTokenSession() 时校验当前是否登录）
     */
    private Boolean tokenSessionCheckLogin = true;

    /**
     * 是否打开自动续签 activeTimeout （如果此值为 true, 框架会在每次直接或间接调用 getLoginId() 时进行一次过期检查与续签操作）
     */
    private Boolean autoRenew = true;

    /**
     * token 前缀, 前端提交 token 时应该填写的固定前缀，格式样例(hdsecurity: Bearer xxxx-xxxx-xxxx-xxxx)
     */
    private String tokenPrefix;

    /**
     * 是否在初始化配置时在控制台打印版本字符画
     */
    private Boolean isPrint = true;

    /**
     * 是否打印操作日志
     */
    private Boolean isLog = false;

    /**
     * 日志等级（trace、debug、info、warn、error、fatal），此值与 logLevelInt 联动
     */
    private String logLevel = "trace";

    /**
     * http basic 认证的默认账号和密码，冒号隔开，格式样例(Hd:123456)
     */
    private String httpBasicAccount = "";

    /**
     * SameOrigin Token 的有效期 (单位：秒)
     */
    private long sameOriginTokenExpireTime = 60 * 60 * 24;

    /**
     * Cookie 配置对象
     */
    public HdCookieConfig cookie = new HdCookieConfig();

    /**
     * 日志等级 int 值（1=trace、2=debug、3=info、4=warn、5=error、6=fatal），此值与 logLevel 联动
     */
    private int logLevelInt = 1;

    public String getSecurityPrefixKey() {
        return securityPrefixKey;
    }

    public HdSecurityConfig setSecurityPrefixKey(String securityPrefixKey) {
        this.securityPrefixKey = securityPrefixKey;
        return this;
    }

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public HdSecurityConfig setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
        return this;
    }

    public long getTokenActiveExpireTime() {
        return tokenActiveExpireTime;
    }

    public HdSecurityConfig setTokenActiveExpireTime(long tokenActiveExpireTime) {
        this.tokenActiveExpireTime = tokenActiveExpireTime;
        return this;
    }

    public Boolean getDynamicActiveExpireTime() {
        return dynamicActiveExpireTime;
    }

    public HdSecurityConfig setDynamicActiveExpireTime(Boolean dynamicActiveExpireTime) {
        this.dynamicActiveExpireTime = dynamicActiveExpireTime;
        return this;
    }

    public Boolean getConcurrent() {
        return isConcurrent;
    }

    public HdSecurityConfig setConcurrent(Boolean concurrent) {
        isConcurrent = concurrent;
        return this;
    }

    public Boolean getShare() {
        return isShare;
    }

    public HdSecurityConfig setShare(Boolean share) {
        isShare = share;
        return this;
    }

    public int getMaxLoginCount() {
        return maxLoginCount;
    }

    public HdSecurityConfig setMaxLoginCount(int maxLoginCount) {
        this.maxLoginCount = maxLoginCount;
        return this;
    }

    public int getMaxTryTimes() {
        return maxTryTimes;
    }

    public HdSecurityConfig setMaxTryTimes(int maxTryTimes) {
        this.maxTryTimes = maxTryTimes;
        return this;
    }

    public Boolean getReadBody() {
        return isReadBody;
    }

    public HdSecurityConfig setReadBody(Boolean readBody) {
        isReadBody = readBody;
        return this;
    }

    public Boolean getReadHeader() {
        return isReadHeader;
    }

    public HdSecurityConfig setReadHeader(Boolean readHeader) {
        isReadHeader = readHeader;
        return this;
    }

    public Boolean getReadCookie() {
        return isReadCookie;
    }

    public HdSecurityConfig setReadCookie(Boolean readCookie) {
        isReadCookie = readCookie;
        return this;
    }

    public Boolean getWriteHeader() {
        return isWriteHeader;
    }

    public HdSecurityConfig setWriteHeader(Boolean writeHeader) {
        isWriteHeader = writeHeader;
        return this;
    }

    public String getTokenStyle() {
        return tokenStyle;
    }

    public HdSecurityConfig setTokenStyle(String tokenStyle) {
        this.tokenStyle = tokenStyle;
        return this;
    }

    public int getDataRefreshPeriod() {
        return dataRefreshPeriod;
    }

    public HdSecurityConfig setDataRefreshPeriod(int dataRefreshPeriod) {
        this.dataRefreshPeriod = dataRefreshPeriod;
        return this;
    }

    public Boolean getTokenSessionCheckLogin() {
        return tokenSessionCheckLogin;
    }

    public HdSecurityConfig setTokenSessionCheckLogin(Boolean tokenSessionCheckLogin) {
        this.tokenSessionCheckLogin = tokenSessionCheckLogin;
        return this;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public HdSecurityConfig setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
        return this;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public HdSecurityConfig setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
        return this;
    }

    public Boolean getPrint() {
        return isPrint;
    }

    public HdSecurityConfig setPrint(Boolean print) {
        isPrint = print;
        return this;
    }

    public Boolean getLog() {
        return isLog;
    }

    public HdSecurityConfig setLog(Boolean log) {
        isLog = log;
        return this;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public HdSecurityConfig setLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public int getLogLevelInt() {
        return logLevelInt;
    }

    public HdSecurityConfig setLogLevelInt(int logLevelInt) {
        this.logLevelInt = logLevelInt;
        return this;
    }

    public String getHttpBasicAccount() {
        return httpBasicAccount;
    }

    public HdSecurityConfig setHttpBasicAccount(String httpBasicAccount) {
        this.httpBasicAccount = httpBasicAccount;
        return this;
    }

    public long getSameOriginTokenExpireTime() {
        return sameOriginTokenExpireTime;
    }

    public HdSecurityConfig setSameOriginTokenExpireTime(long sameOriginTokenExpireTime) {
        this.sameOriginTokenExpireTime = sameOriginTokenExpireTime;
        return this;
    }

    public HdCookieConfig getCookie() {
        return cookie;
    }

    public HdSecurityConfig setCookie(HdCookieConfig cookie) {
        this.cookie = cookie;
        return this;
    }

    @Override
    public String toString() {
        return "HdSecurityConfig{" +
                "securityPrefixKey='" + securityPrefixKey + '\'' +
                ", tokenExpireTime=" + tokenExpireTime +
                ", tokenActiveExpireTime=" + tokenActiveExpireTime +
                ", dynamicActiveExpireTime=" + dynamicActiveExpireTime +
                ", isConcurrent=" + isConcurrent +
                ", isShare=" + isShare +
                ", maxLoginCount=" + maxLoginCount +
                ", maxTryTimes=" + maxTryTimes +
                ", isReadBody=" + isReadBody +
                ", isReadHeader=" + isReadHeader +
                ", isReadCookie=" + isReadCookie +
                ", isWriteHeader=" + isWriteHeader +
                ", tokenStyle='" + tokenStyle + '\'' +
                ", dataRefreshPeriod=" + dataRefreshPeriod +
                ", tokenSessionCheckLogin=" + tokenSessionCheckLogin +
                ", autoRenew=" + autoRenew +
                ", tokenPrefix='" + tokenPrefix + '\'' +
                ", isPrint=" + isPrint +
                ", isLog=" + isLog +
                ", logLevel='" + logLevel + '\'' +
                ", httpBasicAuth='" + httpBasicAccount + '\'' +
                ", cookie=" + cookie +
                ", logLevelInt=" + logLevelInt +
                '}';
    }
}
