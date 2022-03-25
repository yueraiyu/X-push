package com.yeay.xpush.pusher.impl;

import com.yeay.xpush.message.WsMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.WsPushConfig;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yeay
 * @Description 微信客服消息推送器
 * @createTime 2022/03/14 16:32:04
 */
public class WsPusher extends AbstractWeChatPusher implements Pusher<WsMessage, WsPushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsPusher.class);

    private WsPushConfig config;

    public WsPusher(WsPushConfig config) throws IOReactorException {
        super(config);
        setConfig(config);
    }

    @Override
    public WsPushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(WsPushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(WsMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            WxMpKefuMessage wxMpKefuMessage = msg.createMsg();

            wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
        } catch (Exception e) {
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }

        pushResponse.setSuccess(true);
        return pushResponse;
    }
}
