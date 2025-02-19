package cn.youngkbt.hdsecurity.exception;

/**
 * Hd Security 路径非法异常，检查路径不合法时抛出
 *
 * @author Tianke
 * @date 2024/12/30 23:58:22
 * @since 1.0.0
 */
public class HdSecurityPathInvalidException extends HdSecurityException {
    private String path;

    public HdSecurityPathInvalidException(String message) {
        super(message);
    }

    public String getPath() {
        return path;
    }

    public HdSecurityPathInvalidException setPath(String path) {
        this.path = path;
        return this;
    }
}
