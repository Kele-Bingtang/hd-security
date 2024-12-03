package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.hd.RepositoryKeyHelper;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepositoryKV;

import java.util.Set;

/**
 * @author Tianke
 * @date 2024/11/30 15:31:57
 * @since 1.0.0
 */
public class HdSecurityApplication {
    
    public static HdSecurityApplication instance = new HdSecurityApplication();

    public Object query(String key) {
        return HdSecurityManager.getRepository().query(RepositoryKeyHelper.getApplicationKey(key));
    }

    public HdSecurityApplication add(String key, Object value) {
        // 上下文信息默认不过期
        add(key, value, HdSecurityRepositoryKV.NEVER_EXPIRE);
        return this;
    }

    public HdSecurityApplication add(String key, Object value, long expireTime) {
        // 上下文信息默认不过期
        HdSecurityManager.getRepository().add(RepositoryKeyHelper.getApplicationKey(key), value, expireTime);
        return this;
    }

    public HdSecurityApplication remove(String key) {
        HdSecurityManager.getRepository().remove(RepositoryKeyHelper.getApplicationKey(key));
        return this;
    }

    public void clear() {
        for (String key : keys()) {
            remove(key);
        }
    }

    public Set<String> keys() {
        return HdSecurityManager.getRepository().keys(RepositoryKeyHelper.getApplicationKey(""));
    }
}
