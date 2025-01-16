package cn.youngkbt.hdsecurity.repository;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.utils.HdCollectionUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hd Security 持久层 Map 存储
 *
 * @author Tianke
 * @date 2024/11/28 22:33:48
 * @since 1.0.0
 */
public class HdSecurityRepositoryForMap implements HdSecurityRepository {

    /**
     * <p>数据集合</p>
     * <p>如果为 Account Session，则 Key 为 loginId 生成的唯一 Key，Value 为 HdAccountSession 对象</p>
     * <p>如果为 Token Session，则 Key 为 token 生成的唯一 Key，Value 为 HdTokenSession 对象</p>
     */
    public Map<String, Object> dataMap = new ConcurrentHashMap<>();
    /**
     * 存储数据过期时间的集合（单位: 毫秒）, 记录所有 key 的到期时间 （注意存储的是到期时间，不是剩余存活时间）
     */
    public Map<String, Long> expireMap = new ConcurrentHashMap<>();

    @Override
    public Object query(String key) {
        // 尝试先清除过期的数据
        tryClearDataWhenExpire(key);
        return dataMap.get(key);
    }

    @Override
    public void add(String key, Object value, long expireTime) {
        // 验证 expireTime
        if (0 == expireTime || expireTime < HdSecurityRepositoryKV.NOT_VALUE_EXPIRE) {
            return;
        }
        dataMap.put(key, value);
        expireMap.put(key, getExpireTimeMillis(expireTime));
    }

    @Override
    public void edit(String key, Object value) {
        long expireTime = getExpireTime(key);
        if (expireTime == HdSecurityRepositoryKV.NOT_VALUE_EXPIRE) {
            return;
        }

        dataMap.put(key, value);
    }

    @Override
    public void remove(String key) {
        dataMap.remove(key);
        expireMap.remove(key);
    }

    @Override
    public void clear() {
        dataMap.clear();
        expireMap.clear();
    }

    @Override
    public long getExpireTime(String key) {
        Long expireTime = expireMap.get(key);
        // 如果 expire 数据不存在，说明框架没有存储这个 key，此时返回 NOT_VALUE_EXPIRE
        if (null == expireTime) {
            return HdSecurityRepositoryKV.NOT_VALUE_EXPIRE;
        }

        // 如果 expire 被标注为永不过期，则返回 NEVER_EXPIRE
        if (expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE) {
            return HdSecurityRepositoryKV.NEVER_EXPIRE;
        }

        long currentExpireTime = getCurrentExpireTime(key);

        if (currentExpireTime < 0) {
            remove(key);
            return HdSecurityRepositoryKV.NOT_VALUE_EXPIRE;
        }

        return currentExpireTime;
    }

    @Override
    public void updateExpireTime(String key, long expireTime) {
        expireMap.put(key, getExpireTimeMillis(expireTime));
    }

    @Override
    public void init() {
        initRefreshThread();
    }

    @Override
    public List<String> searchKeyList(String prefix, String keyword, int start, int size, boolean sortType) {
        return HdCollectionUtil.searchList(expireMap.keySet(), prefix, keyword, start, size, sortType);
    }

    /**
     * 获取实际的过期时间（单位：毫秒）
     *
     * @param expireTime 过期时间
     * @return 过期时间（单位：毫秒）
     */
    public long getExpireTimeMillis(long expireTime) {
        return expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE ? HdSecurityRepositoryKV.NEVER_EXPIRE : System.currentTimeMillis() + expireTime * 1000;
    }

    /**
     * 获取当前 key 的剩余过期时间
     *
     * @param key key
     * @return key 的剩余过期时间
     */
    public long getCurrentExpireTime(String key) {
        return (expireMap.get(key) - System.currentTimeMillis()) / 1000;
    }

    /**
     * 尝试清理过期数据
     *
     * @param key key
     */
    public void tryClearDataWhenExpire(String key) {
        Long expireTime = expireMap.get(key);

        // 如果 key 不存在过期时长或 key 为永久有效，则不处理
        if (null == expireTime || expireTime == HdSecurityRepositoryKV.NEVER_EXPIRE) {
            return;
        }

        // 如果当前时间大于过期时间，则代表过期，直接删除
        if (System.currentTimeMillis() > expireTime) {
            remove(key);
        }
    }

    /**
     * 执行数据清理的线程引用
     */
    public Thread refreshThread;

    /**
     * 是否继续执行数据清理的线程标记
     */
    public volatile boolean refreshFlag;

    /**
     * 初始化定时任务，定时清理过期数据
     */
    public void initRefreshThread() {
        int dataRefreshPeriod = HdSecurityManager.getConfig().getDataRefreshPeriod();
        // 如果配置了 <=0 的值，则不启动定时清理
        if (dataRefreshPeriod <= 0) {
            return;
        }

        // 启动定时刷新
        this.refreshFlag = true;
        this.refreshThread = new Thread(() -> {
            for (; ; ) {
                try {
                    // 如果已经被标记为结束
                    if (!refreshFlag) {
                        return;
                    }
                    // 执行清理
                    for (String key : expireMap.keySet()) {
                        tryClearDataWhenExpire(key);
                    }
                    // 休眠 N 秒 
                    Thread.sleep(dataRefreshPeriod * 1000L);
                } catch (InterruptedException e) {
                    // 重新中断当前线程
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
        this.refreshThread.start();
    }

}
