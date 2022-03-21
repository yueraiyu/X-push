package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.AliSmsPusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 阿里云短信推送配置
 * @createTime 2022/03/14 11:02:03
 */
@Getter
@Setter
@ToString
public class AliSmsPushConfig implements PusherConfig {
    /**
     * 用户标识ID
     */
    private String accessKeyId;

    /**
     * 用户密钥
     */
    private String accessKeySecret;


    /**
     * 短信签名名称
     */
    private String signName;

    /**
     * 短信模板编号
     */
    private String templateCode;

    @Override
    public Pusher configPusher() {
        return new AliSmsPusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_ALI_SMS;
    }
}
