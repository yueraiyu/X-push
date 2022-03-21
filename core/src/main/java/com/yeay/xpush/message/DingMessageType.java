package com.yeay.xpush.message;

/**
 * @author yeay
 * @Description 钉钉消息类型
 * @createTime 2022/03/14 14:45:15
 */
public enum DingMessageType {
    /**
     * 文本消息
     */
    TEXT("text"),

    /**
     * 链接消息
     */
    LINK("link"),

    /**
     * markdown消息
     */
    MARKDOWN("markdown"),

    /**
     * 卡片消息
     */
    ACTION_CARD("actionCard");

    private String name;

    DingMessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
