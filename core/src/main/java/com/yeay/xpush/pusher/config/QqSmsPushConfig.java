package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.QqSmsPusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 腾讯短信推送配置
 * @createTime 2022/03/14 11:58:13
 */
@Setter
@Getter
@ToString
public class QqSmsPushConfig implements PusherConfig {

    /**
     * 应用标识 id
     */
    private String appId;

    /**
     * 应用标识 key
     */
    private String appKey;

    /**
     * 短信模板 id
     */
    private int templateId;

    /**
     * 签名
     */
    private String signName;

    @Override
    public Pusher configPusher() {
        return new QqSmsPusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_QQ_SMS;
    }
}
