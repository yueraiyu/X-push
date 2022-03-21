package com.yeay.xpush.message;

/**
 * @author yeay
 * @Description 钉钉推送渠道类型
 * @createTime 2022/03/14 14:33:32
 */
public enum DingPushType {
    /**
     * 工作消息
     */
    WORK("work"),

    /**
     * 机器人消息
     */
    ROBOT("robot");

    private String name;

    DingPushType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
