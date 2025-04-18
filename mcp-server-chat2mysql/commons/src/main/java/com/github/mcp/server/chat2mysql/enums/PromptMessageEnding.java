package com.github.mcp.server.chat2mysql.enums;

public enum PromptMessageEnding {

    en_US("en_US", "Please answer in English"),

    zh_CN("zh_CN", "请使用中文回答"),

    ;

    private final String code;

    private final String prompt;

    PromptMessageEnding(String code, String prompt) {
        this.code = code;
        this.prompt = prompt;
    }

    public static String ofCurrentUserLanguage() {
        PromptMessageEnding[] values = values();
        for (PromptMessageEnding value : values) {
            final String language = System.getProperty("user.language");
            final String country = System.getProperty("user.country");
            final String locale = String.format("%s_%s", language, country);
            if (value.code.equals(locale)) {
                return value.prompt;
            }
        }
        return en_US.prompt;
    }

}
