package com.yeay.xpush.message;

/**
 * @author yeay
 * @Description 微信客服消息类型
 * @createTime 2022/03/14 17:21:18
 */
public enum WsMessageType {
    /**
     * 图文消息
     */
    NEWS("news"),

    /**
     * 文本消息
     */
    TEXT("text"),

    /**
     * 小程序卡片
     */
    MINI_PROGRAM_CARD("mini_program_card");

    private String name;

    WsMessageType(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
