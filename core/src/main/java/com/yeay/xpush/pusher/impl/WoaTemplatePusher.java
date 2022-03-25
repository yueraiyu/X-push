package com.yeay.xpush.pusher.impl;

import com.yeay.xpush.message.WoaTemplateMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.WoaTemplatePushConfig;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yeay
 * @Description 微信公众号模板消息推送器
 * @createTime 2022/03/14 17:55:16
 */
public class WoaTemplatePusher extends AbstractWeChatPusher implements Pusher<WoaTemplateMessage, WoaTemplatePushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WoaTemplatePusher.class);

    private WoaTemplatePushConfig config;

    public WoaTemplatePusher(WoaTemplatePushConfig config) throws IOReactorException {
        super(config);
        setConfig(config);
    }

    @Override
    public WoaTemplatePushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(WoaTemplatePushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(WoaTemplateMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            WxMpTemplateMessage wxMessageTemplate = msg.createMsg();
            if (StringUtils.isNotEmpty(getConfig().getTemplateId())
                    && StringUtils.isEmpty(wxMessageTemplate.getTemplateId())) {
                wxMessageTemplate.setTemplateId(getConfig().getTemplateId());
            }

            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMessageTemplate);
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
