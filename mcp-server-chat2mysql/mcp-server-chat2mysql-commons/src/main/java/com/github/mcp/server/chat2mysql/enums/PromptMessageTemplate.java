package com.github.mcp.server.chat2mysql.enums;

public enum PromptMessageTemplate {

    en_US("en_US", """
        There is an SQL statement along with its EXPLAIN plan and table schemas information.

        The SQL statement is: {0}

        The table schemas information are as follows: {1}

        The EXPLAIN plan for the SQL statement is: {2}

        Please analyze the query performance and provide optimization recommendations for the SQL statement.

        Answer me in English.
        """),

    zh_CN("zh_CN", """
        现在有一条SQL语句以及相关的表结构信息和它的EXPLAIN执行计划。

        这是SQL语句的内容：{0}

        涉及到的表结构信息如下：{1}

        SQL EXPLAIN 执行计划的结果是：{2}

        请分析这条SQL语句的查询性能并给出优化建议。

        用简体中文回答。
        """),

    ;

    private final String code;

    private final String template;

    PromptMessageTemplate(String code, String template) {
        this.code = code;
        this.template = template;
    }

    public static String getBySystemLanguage() {
        PromptMessageTemplate[] values = values();
        for (PromptMessageTemplate value : values) {
            final String language = System.getProperty("user.language");
            final String country = System.getProperty("user.country");
            final String locale = String.format("%s_%s", language, country);
            if (value.code.equals(locale)) {
                return value.template;
            }
        }
        return en_US.template;
    }

}
