package cn.youngkbt.hdsecurity.model.session;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hd Security 设备，也是端口，用于存储设备信息
 * 
 * @author Tianke
 * @date 2024/11/26 23:15:37
 * @since 1.0.0
 */
public class HdTokenDevice implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Token 值
     */
    private String token;

    /**
     * 所属设备类型
     */
    private String device;

    /**
     * 设备存储的数据
     */
    private Map<String, Object> data = new ConcurrentHashMap<>();

    public HdTokenDevice() {
    }

    public HdTokenDevice(String token, String device, Map<String, Object> data) {
        this.token = token;
        this.device = device;
        this.data = data;
    }

    public HdTokenDevice(String token, String device) {
        this.token = token;
        this.device = device;
    }

    public String getToken() {
        return token;
    }

    public HdTokenDevice setToken(String token) {
        this.token = token;
        return this;
    }

    public String getDevice() {
        return device;
    }

    public HdTokenDevice setDevice(String device) {
        this.device = device;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public HdTokenDevice setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
