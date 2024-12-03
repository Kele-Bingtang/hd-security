package cn.youngkbt.hdsecurity.model.session;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.constants.DefaultConstant;
import cn.youngkbt.hdsecurity.hd.RepositoryKeyHelper;
import cn.youngkbt.hdsecurity.listener.HdSecurityEventCenter;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个账号的一个
 *
 * @author Tianke
 * @date 2024/11/26 21:56:08
 * @since 1.0.0
 */
public class HdSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * HdSession 的 id
     */
    private String id;
    /**
     * Session 类型
     */
    private String type;
    /**
     * HdSession 的账号类型
     */
    private String accountType;
    /**
     * HdSession 存储的数据
     */
    private Map<String, Object> attribute = new ConcurrentHashMap<>();
    /**
     * HdSession 创建时间
     */
    private LocalDateTime createTime;
    /**
     * HdSession 最后一次更新时间
     */
    private LocalDateTime lastUpdateTime;
    /**
     * HdSession 设备列表
     */
    private List<HdTokenDevice> tokenDeviceList = new ArrayList<>();

    public HdSession(String id) {
        this(id, DefaultConstant.DEFAULT_ACCOUNT_TYPE);
    }

    public HdSession(String id, String accountType) {
        // 发布创建 Session 开始事件
        HdSecurityEventCenter.publishBeforeCreateSession(id);

        this.id = RepositoryKeyHelper.getAccountSessionKey(id, accountType);
        this.accountType = HdStringUtil.hasEmpty(accountType) ? DefaultConstant.DEFAULT_ACCOUNT_TYPE : accountType;
        this.createTime = LocalDateTime.now();
        this.lastUpdateTime = LocalDateTime.now();

        // 发布创建 Session 结束事件
        HdSecurityEventCenter.publishAfterCreateSession(id);
    }

    public void updateTime() {
        this.lastUpdateTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public HdSession setId(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public HdSession setType(String type) {
        this.type = type;
        return this;
    }

    public String getAccountType() {
        return accountType;
    }

    public HdSession setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public HdSession setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
        return this;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public HdSession setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public HdSession setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    public List<HdTokenDevice> getTokenDeviceList() {
        return tokenDeviceList;
    }

    // ---------- Token Device 相关操作方法 ----------

    public HdSession setTokenDeviceList(List<HdTokenDevice> tokenDeviceList) {
        this.tokenDeviceList = tokenDeviceList;
        return this;
    }

    public HdTokenDevice getTokenDeviceByToken(String token) {
        return tokenDeviceList.stream()
                .filter(d -> d.getToken().equals(token))
                .findFirst()
                .orElse(null);
    }

    public List<HdTokenDevice> getTokenDeviceListByDevice(String device) {
        if (HdStringUtil.hasEmpty(device)) {
            return new ArrayList<>(tokenDeviceList);
        }

        return tokenDeviceList.stream()
                .filter(d -> d.getDevice().equals(device))
                .toList();

    }

    public List<String> getTokenListByDevice(String device) {
        if (HdStringUtil.hasEmpty(device)) {
            return tokenDeviceList.stream().map(HdTokenDevice::getToken).toList();
        }

        List<HdTokenDevice> devices = tokenDeviceList.stream()
                .filter(d -> d.getDevice().equals(device))
                .toList();

        return devices.stream().map(HdTokenDevice::getToken).toList();
    }

    public HdTokenDevice getTokenDevice(HdTokenDevice tokenDevice) {
        return getTokenDeviceByToken(tokenDevice.getToken());

    }

    public void addDevice(HdTokenDevice device) {
        HdTokenDevice oldDevice = getTokenDevice(device);
        if (null == oldDevice) {
            tokenDeviceList.add(device);
            updateToRepository();
        } else {
            // 存在则更新
            HdTokenDevice newDevice = new HdTokenDevice()
                    .setToken(device.getToken())
                    .setDevice(device.getDevice())
                    .setData(device.getData());

            int index = tokenDeviceList.indexOf(oldDevice);
            if (index != -1) {
                tokenDeviceList.set(index, newDevice);
            }
            updateToRepository();
        }
    }

    public void removeTokenDevice(String token) {
        HdTokenDevice tokenDevice = getTokenDeviceByToken(token);
        if (tokenDeviceList.remove(tokenDevice)) {
            updateToRepository();
        }
    }

    // --------- 持久层操作相关方法 ---------

    public void updateToRepository() {
        HdSecurityManager.getRepository().editSession(this);
    }

    public void removeFromRepository() {
        // 发布注销 Session 开始事件
        HdSecurityEventCenter.publishBeforeLogoutSession(id);

        HdSecurityManager.getRepository().removeSession(this.id);
        
        // 发布注销 Session 结束事件
        HdSecurityEventCenter.publishAfterCreateSession(id);
    }

    public long getExpireTime() {
        return HdSecurityManager.getRepository().getSessionTimeout(this.id);
    }

    public void updateExpireTime(long expireTime) {
        HdSecurityManager.getRepository().updateSessionTimeout(this.id, expireTime);
    }

    /**
     * 更新 Session 的存活时间。当 lessThan 为 true 时，如果当前 Session 的存活时间小于参数 time，则不更新，否则更新。
     *
     * @param time     新的存活时间
     * @param lessThan 当前 Session 的存活时间是否小于参数 time
     */
    public void updateExpireTimeWhenCondition(long time, boolean lessThan) {
        long newTime = trans(time);
        long currentExpireTime = trans(getExpireTime());
        if (lessThan) {
            if (currentExpireTime < newTime) {
                updateExpireTime(newTime);
            }
        } else {
            if (currentExpireTime > newTime) {
                updateExpireTime(newTime);
            }
        }

    }

    /**
     * value为 -1 时返回 Long.MAX_VALUE，否则原样返回
     *
     * @param value 值
     * @return 转换后的值
     */
    protected long trans(long value) {
        return value == HdSecurityRepository.NEVER_EXPIRE ? Long.MAX_VALUE : value;
    }

    // ---------- attribute 操作相关方法 ---------
    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public void setAttribute(String key, Object value) {
        attribute.put(key, value);
        updateToRepository();
    }

    public void setAttributeIfPresent(String key, Object value) {
        if (null != value) {
            attribute.put(key, value);
            updateToRepository();
        }
    }

    public void removeAttribute(String key) {
        attribute.remove(key);
        updateToRepository();
    }

    public void clearAttributes() {
        attribute.clear();
        updateToRepository();
    }

    public Set<String> getAttributeKeys() {
        return attribute.keySet();
    }

}
