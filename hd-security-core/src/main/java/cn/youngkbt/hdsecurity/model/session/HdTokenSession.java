package cn.youngkbt.hdsecurity.model.session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tianke
 * @date 2024/11/26 22:23:03
 * @since 1.0.0
 */
public class HdTokenSession extends HdSession {
    private String token;

    public HdTokenSession(String token) {
        this(token, null);
    }

    public HdTokenSession(String token, String accountType) {
        super(token, accountType);
        this.token = token;
        setToken("token");
    }

    public String getToken() {
        return token;
    }

    public HdTokenSession setToken(String token) {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {

            });
        }


        this.token = token;
        return this;
    }
}
