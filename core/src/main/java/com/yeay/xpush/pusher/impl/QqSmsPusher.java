package com.yeay.xpush.pusher.impl;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.yeay.xpush.message.QqSmsMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.QqSmsPushConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yeay
 * @Description 腾讯云短信推送
 * @createTime 2022/03/14 11:58:36
 */
public class QqSmsPusher implements Pusher<QqSmsMessage, QqSmsPushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QqSmsPusher.class);

    private QqSmsPushConfig config;

    private volatile SmsSingleSender smsSingleSender;

    public QqSmsPusher(QqSmsPushConfig config) {
        setConfig(config);

        initSender();
    }

    private void initSender() {
        if (smsSingleSender == null) {
            synchronized (QqSmsPusher.class) {
                if (smsSingleSender == null) {
                    String appId = getConfig().getAppId();
                    String appKey = getConfig().getAppKey();

                    smsSingleSender = new SmsSingleSender(Integer.parseInt(appId), appKey);
                }
            }
        }
    }

    @Override
    public QqSmsPushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(QqSmsPushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(QqSmsMessage msg) {
        PushResponse pushResponse = new PushResponse();
        try {
            String[] params = msg.createMsg();

            SmsSingleSenderResult result = smsSingleSender.sendWithParam("86", msg.getTelephoneNum(),
                    getConfig().getTemplateId(), params, getConfig().getSignName(), "", "");

            if (result.result == 0) {
                pushResponse.setSuccess(true);
            } else {
                LOGGER.error("push message fail! {}", result.toString());
                pushResponse.setSuccess(false);
                pushResponse.setMessage(result.toString());
            }
        } catch (Exception e) {
            LOGGER.error("push message fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
        }

        return pushResponse;
    }
}
