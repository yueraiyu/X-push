package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.WoaTemplatePusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * @author yeay
 * @Description 微信公众号模板消息推送配置
 * @createTime 2022/03/14 17:53:33
 */
@Setter
@Getter
@ToString
public class WoaTemplatePushConfig extends AbstractWeChatPushConfig implements PusherConfig {
    @Override
    public Pusher configPusher() throws IOReactorException {
        return new WoaTemplatePusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_WOA_TEMPLATE;
    }
}
