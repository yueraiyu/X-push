package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.QwPusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * @author yeay
 * @Description 企业微信推送配置
 * @createTime 2022/03/14 15:18:15
 */
@Setter
@Getter
@ToString
public class QwPushConfig implements PusherConfig {

    /**
     * 企业id
     */
    private String corpId;

    /**
     * 内部应用 id
     */
    private String agentId;

    /**
     * 内部应用的凭证密钥
     */
    private String corpSecret;

    /**
     * 群机器人 调用地址
     */
    private String webHook;

    @Override
    public Pusher configPusher() throws IOReactorException {
        return new QwPusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_QW;
    }
}
