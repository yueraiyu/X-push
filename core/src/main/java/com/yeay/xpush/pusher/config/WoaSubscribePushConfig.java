package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.WoaSubscribePusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * @author yeay
 * @Description 微信公众号订阅消息推送
 * @createTime 2022/03/14 17:37:35
 */
@Setter
@Getter
@ToString
public class WoaSubscribePushConfig extends AbstractWeChatPushConfig implements PusherConfig {

    @Override
    public Pusher configPusher() throws IOReactorException {
        return new WoaSubscribePusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_WOA_SUBSCRIBE;
    }
}
