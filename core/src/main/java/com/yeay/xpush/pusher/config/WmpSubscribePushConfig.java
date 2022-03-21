package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.WmpSubscribePusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 微信小程序订阅消息推送配置
 * @createTime 2022/03/14 18:08:38
 */
@Setter
@Getter
@ToString
public class WmpSubscribePushConfig implements PusherConfig {

    /**
     * 小程序应用标识 id
     */
    private String miniAppAppId;

    /**
     * 小程序应用凭证
     */
    private String miniAppAppSecret;

    private String miniAppToken;

    /**
     * 消息加密密钥
     */
    private String miniAppAesKey;

    /**
     * 消息模板id
     */
    private String templateId;

    @Override
    public Pusher configPusher() {
        return new WmpSubscribePusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_WMP_SUBSCRIBE;
    }
}
