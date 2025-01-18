package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.hd.RepositoryKeyHelper;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;

import java.util.ArrayList;
import java.util.List;

/**
 * Hd Security 全局应用域，可以利用这个类来存储一些全局信息。应用重启后数据会丢失。
 *
 * @author Tianke
 * @date 2024/11/30 15:31:57
 * @since 1.0.0
 */
public class HdSecurityApplication {

    public static HdSecurityApplication instance = new HdSecurityApplication();

    /**
     * 获取全局应用域信息
     *
     * @param key
     * @return
     */
    public Object query(String key) {
        return HdSecurityManager.getRepository().query(RepositoryKeyHelper.getApplicationKey(key));
    }

    /**
     * 添加全局应用域信息
     *
     * @param key   键
     * @param value 值
     * @return 对象本身
     */
    public HdSecurityApplication add(String key, Object value) {
        // 上下文信息默认不过期
        add(key, value, HdSecurityRepositoryKV.NEVER_EXPIRE);
        return this;
    }

    /**
     * 添加全局应用域信息，指定过期时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间
     * @return 对象本身
     */
    public HdSecurityApplication add(String key, Object value, long expireTime) {
        // 上下文信息默认不过期
        HdSecurityManager.getRepository().add(RepositoryKeyHelper.getApplicationKey(key), value, expireTime);
        return this;
    }

    /**
     * 移除全局应用域信息
     *
     * @param key 键
     * @return 对象本身
     */
    public HdSecurityApplication remove(String key) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getApplicationKey(key));
        return this;
    }

    /**
     * 清空全局应用域信息
     *
     */
    public void clear() {
        for (String key : keys()) {
            remove(key);
        }
    }

    /**
     * 获取全局应用域信息键集合
     *
     * @return 键集合
     */
    public List<String> keys() {
        String applicationKey = RepositoryKeyHelper.getApplicationKey("");
        List<String> keyList = HdSecurityManager.getRepository().searchKeyList(applicationKey, "", 0, -1, true);

        // 裁减掉固定前缀，保留 key 名称，塞入新集合
        int prefixLength = applicationKey.length();
        List<String> finalKeyList = new ArrayList<>();
        if(null != keyList) {
            for (String key : keyList) {
                finalKeyList.add(key.substring(prefixLength));
            }
        }

        // 返回 
        return finalKeyList;
    }
}
