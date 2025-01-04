package cn.youngkbt.hdsecurity.strategy;

import cn.youngkbt.hdsecurity.hd.HdTokenHelper;

import java.util.function.Supplier;

/**
 * Token 生成方式枚举
 *
 * @author Tianke
 * @date 2024/11/28 00:27:57
 * @since 1.0.0
 */
public enum HdSecurityTokenGenerateEnums {
    UUID("uuid", HdTokenHelper::createUuidToken),
    SIMPLE_UUID("simple-uuid", HdTokenHelper::createSimpleUuidToken),
    RANDOM_32("random-32", HdTokenHelper::createRandom32Token),
    RANDOM_64("random-64", HdTokenHelper::createRandom64Token),
    RANDOM_128("random-128", HdTokenHelper::createRandom128Token),
    TIK("tik", HdTokenHelper::createTlkToken);

    private final String style;
    private final Supplier<String> generator;

    public static String getTokenByStyle(String style) {
        for (HdSecurityTokenGenerateEnums enums : HdSecurityTokenGenerateEnums.values()) {
            if (enums.getStyle().equals(style)) {
                return enums.getGenerator().get();
            }
        }
        return null;
    }

    HdSecurityTokenGenerateEnums(String style, Supplier<String> generator) {
        this.style = style;
        this.generator = generator;
    }

    public String getStyle() {
        return style;
    }

    public Supplier<String> getGenerator() {
        return generator;
    }
}
