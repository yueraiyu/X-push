package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.DingPusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author yeay
 * @Description 钉钉推送配置
 * @createTime 2022/03/14 14:20:09
 */
@Setter
@Getter
@ToString
public class DingPushConfig implements PusherConfig {

    /**
     * 微应用 ID
     */
    private String agentId;

    /**
     * 应用的唯一标识key
     */
    private String appKey;

    /**
     * 应用的密钥
     */
    private String appSecret;

    /**
     * 群机器人调用地址
     */
    private String webHook;

    /**
     * 群机器人安全配置-关键字 最多10个
     */
    private List<String> robotKeyWords;

    /**
     * 群机器人安全配置-加签（密钥）
     */
    private String robotSecret;

    @Override
    public Pusher configPusher() {
        return new DingPusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_DING;
    }
}
