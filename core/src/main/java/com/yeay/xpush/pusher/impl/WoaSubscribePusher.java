package com.yeay.xpush.pusher.impl;

import com.yeay.xpush.message.WoaSubscribeMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.WoaSubscribePushConfig;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yeay
 * @Description 微信公众号订阅消息推送器
 * @createTime 2022/03/14 17:38:03
 */
public class WoaSubscribePusher extends AbstractWeChatPusher implements Pusher<WoaSubscribeMessage, WoaSubscribePushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WoaSubscribePusher.class);

    private WoaSubscribePushConfig config;

    public WoaSubscribePusher(WoaSubscribePushConfig config) throws IOReactorException {
        super(config);
        setConfig(config);
    }

    @Override
    public WoaSubscribePushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(WoaSubscribePushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(WoaSubscribeMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            WxMpSubscribeMessage wxMessageTemplate = msg.createMsg();
            if (StringUtils.isNotEmpty(getConfig().getTemplateId())
                    && StringUtils.isEmpty(wxMessageTemplate.getTemplateId())) {
                wxMessageTemplate.setTemplateId(getConfig().getTemplateId());
            }

            wxMpService.getSubscribeMsgService().send(wxMessageTemplate);
        } catch (Exception e) {
            LOGGER.error("push message fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }

        pushResponse.setSuccess(true);
        return pushResponse;
    }
}
