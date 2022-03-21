package com.yeay.xpush.message;

/**
 * @author yeay
 * @Description 微信消息类型
 * @createTime 2022/03/14 14:45:15
 */
public enum QwMessageType {
    /**
     * 图文消息
     */
    NEWS("news"),

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
    TEXT_CARD("text_card"),

    /**
     * 图片消息
     */
    IMAGE("image");

    private String name;

    QwMessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
