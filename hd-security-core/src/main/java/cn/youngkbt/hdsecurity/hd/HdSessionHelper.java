package cn.youngkbt.hdsecurity.hd;

import cn.youngkbt.hdsecurity.HdSecurityManager;
import cn.youngkbt.hdsecurity.config.HdSecurityConfig;
import cn.youngkbt.hdsecurity.config.HdSecurityConfigProvider;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecuritySessionException;
import cn.youngkbt.hdsecurity.model.login.HdLoginModel;
import cn.youngkbt.hdsecurity.model.login.HdLoginModelOperator;
import cn.youngkbt.hdsecurity.model.session.HdSession;
import cn.youngkbt.hdsecurity.model.session.HdTokenDevice;
import cn.youngkbt.hdsecurity.repository.HdSecurityRepository;
import cn.youngkbt.hdsecurity.strategy.SessionCreateStrategy;
import cn.youngkbt.hdsecurity.utils.HdStringUtil;

import java.util.Optional;

/**
 * @author Tianke
 * @date 2024/11/28 01:26:59
 * @since 1.0.0
 */
public class HdSessionHelper {
    private final String accountType;

    public HdSessionHelper(String accountType) {
        this.accountType = accountType;
    }

    /**
     * 创建账号会话
     *
     * @param hdLoginModel 登录模型
     * @return token
     */
    public String createAccountSession(HdLoginModel hdLoginModel) {
        HdLoginModel loginModel = HdLoginModelOperator.mutate(hdLoginModel);

        Object loginId = loginModel.getLoginId();
        Long tokenExpireTime = loginModel.getTokenExpireTime();

        // 检查登录模型
        HdHelper.loginHelper(accountType).checkLoginModel(loginModel);

        HdSecurityConfig config = HdSecurityManager.getConfig();
        // 如果不允许一个账号多地同时登录，则需要先将这个账号的历史登录会话标记为 被顶下线
        if (Boolean.FALSE.equals(config.getConcurrent())) {
            // TODO 顶人下线
        }

        // 获取 Account Session 会话，如果获取失败，则代表第一次登录，需要创建新的会话
        HdSession session = getAccountSessionByLoginIdOrCreate(loginId, tokenExpireTime);

        // 创建 Token
        HdTokenHelper tokenHelper = HdHelper.tokenHelper(accountType);
        String token = tokenHelper.createToken(loginModel);

        // 创建设备，一个设备持有一个 Token
        HdTokenDevice tokenDevice = new HdTokenDevice(token, loginModel.getDevice(), loginModel.getTokenDeviceData());
        // 在 Account Session 记录登录的设备
        session.addDevice(tokenDevice);
        
        /*
          保存 token -> loginId 的映射关系，方便日后根据 token 找 loginId
          场景 1：通过 token 查找所在的 Account Session，需要遍历所有 Account Session 下的 TokenDevice 集合。比较麻烦，所以创建映射关系，通过 token 先找到 loginId，接着根据 loginId 可以直接遍历出匹配的 Account Session
          场景 2：校验 Token 是否已经被创建使用时，只需要通过 Token 找到 loginId，找不到则代表没有登录过，token 也就没有被创建，找到就代表 token 已经被使用，不再是唯一
          @see  HdTokenHelper#createToken(HdLoginModel)
         */
        tokenHelper.addTokenAndLoginIdMapping(loginId, token, loginModel.getTokenExpireTime());

        // 更新 Token 最后活跃时间
        if (HdSecurityConfigProvider.isUseActiveTimeout()) {
            tokenHelper.addTokenActiveTime(token, loginModel.getTokenActiveExpireTime(), tokenExpireTime);
        }

        // 检查此账号会话数量是否超出最大值，如果超过，则按照登录时间顺序，把最开始登录的给注销掉
        if (config.getMaxLoginCount() != -1) {
            HdHelper.loginHelper(accountType).logoutByMaxLoginCount(loginId, session, null, config.getMaxLoginCount());
        }

        return token;
    }

    /**
     * 获取账号会话，如果不存在账号会话，则创建一个
     *
     * @param loginId 登录 ID
     * @return 账号会话
     */
    public HdSession getAccountSessionByLoginIdOrCreate(Object loginId, Long expireTime) {
        if (HdStringUtil.hasEmpty(loginId)) {
            throw new HdSecuritySessionException("Account-Session 获取失败：loginId 不能为空").setCode(HdSecurityErrorCode.LOGIN_ID_IS_NULL);
        }

        HdSecurityRepository repository = HdSecurityManager.getRepository();
        HdSession session = repository.querySession(RepositoryKeyHelper.getAccountSessionKey(loginId, accountType));

        if (null == session) {
            // 策略模式创建 Account Session
            session = SessionCreateStrategy.instance.createSession.apply(String.valueOf(loginId));
            Long tokenExpireTime = Optional.ofNullable(expireTime).orElse(HdSecurityManager.getConfig().getTokenExpireTime());

            // 存储到持久层
            repository.addSession(session, tokenExpireTime);
        } else {
            // 在持久层更新缓存的时间
            session.updateExpireTimeWhenCondition(expireTime, true);
        }
        return session;
    }

    /**
     * 根据登录 ID 获取账号会话
     *
     * @param loginId 登录 ID
     * @return 账号会话
     */
    public HdSession getAccountSessionByLoginId(Object loginId) {
        if (HdStringUtil.hasEmpty(loginId)) {
            throw new HdSecuritySessionException("Account-Session 获取失败：loginId 不能为空").setCode(HdSecurityErrorCode.LOGIN_ID_IS_NULL);
        }

        return HdSecurityManager.getRepository().querySession(RepositoryKeyHelper.getAccountSessionKey(loginId, accountType));
    }

    /**
     * 根据 Token 获取 Token 会话
     *
     * @param token       Token
     * @param accountType 账号类型
     */
    public void removeTokenSession(String token, String accountType) {
        HdSecurityManager.getRepository().removeSession(RepositoryKeyHelper.getTokenSessionKey(token, accountType));
    }

}
