package cn.youngkbt.hdsecurity.repository;

import cn.youngkbt.hdsecurity.model.session.HdSession;

/**
 * @author Tianke
 * @date 2024/11/29 00:13:30
 * @since 1.0.0
 */
public interface HdSecurityRepository extends HdSecurityRepositoryKV<String, Object> {
    /**
     * 获取 SaSession，如无返空
     *
     * @param sessionId sessionId
     * @return SaSession
     */
    default HdSession querySession(String sessionId) {
        return (HdSession) query(sessionId);
    }

    /**
     * 写入 SaSession，并设定存活时间（单位: 秒）
     *
     * @param session 要保存的 SaSession 对象
     * @param timeout 过期时间（单位: 秒）
     */
    default void addSession(HdSession session, long timeout) {
        add(session.getId(), session, timeout);
    }

    /**
     * 更新 SaSession
     *
     * @param session 要更新的 SaSession 对象
     */
    default void editSession(HdSession session) {
        edit(session.getId(), session);
    }

    /**
     * 删除 SaSession
     *
     * @param sessionId sessionId
     */
    default void removeSession(String sessionId) {
        remove(sessionId);
    }

    /**
     * 获取 SaSession 剩余存活时间（单位: 秒）
     *
     * @param sessionId 指定 SaSession
     * @return 这个 SaSession 的剩余存活时间
     */
    default long getSessionTimeout(String sessionId) {
        return getExpireTime(sessionId);
    }

    /**
     * 修改 SaSession 剩余存活时间（单位: 秒）
     *
     * @param sessionId 指定 SaSession
     * @param timeout   剩余存活时间
     */
    default void updateSessionTimeout(String sessionId, long timeout) {
        updateExpireTime(sessionId, timeout);
    }
}
