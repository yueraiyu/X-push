package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.WsPusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * @author yeay
 * @Description 微信客服消息推送配置
 * @createTime 2022/03/14 16:31:04
 */
@Setter
@Getter
@ToString
public class WsPushConfig extends AbstractWeChatPushConfig implements PusherConfig {

    @Override
    public Pusher configPusher() throws IOReactorException {
        return new WsPusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_WS;
    }
}
