package com.yeay.xpush.pusher.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.yeay.xpush.message.EmailMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.EmailPushConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author yeay
 * @Description 邮件推送器
 * @createTime 2022/03/14 18:24:57
 */
public class EmailPusher implements Pusher<EmailMessage, EmailPushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPusher.class);

    private EmailPushConfig config;

    private volatile MailAccount mailAccount;

    public EmailPusher(EmailPushConfig config) {
        this.config = config;

        initMailAccount();
    }

    @Override
    public EmailPushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(EmailPushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(EmailMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            if (CollectionUtils.isEmpty(msg.getFiles())) {
                MailUtil.send(mailAccount, msg.getTos(), msg.getCcs(), msg.getBccs(),
                        msg.getTitle(), msg.getContent(), true);
            } else {
                MailUtil.send(mailAccount, msg.getTos(), msg.getCcs(), msg.getBccs(),
                        msg.getTitle(), msg.getContent(), true, msg.getFiles().toArray(new File[0]));
            }

            pushResponse.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error("push message fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
        }

        return pushResponse;
    }

    private MailAccount initMailAccount() {
        if (mailAccount == null) {
            synchronized (EmailPusher.class) {
                if (mailAccount == null) {
                    mailAccount = new MailAccount();
                    mailAccount.setHost(getConfig().getHost());
                    mailAccount.setPort(Integer.valueOf(getConfig().getPort()));
                    mailAccount.setAuth(true);
                    mailAccount.setFrom(getConfig().getFrom());
                    mailAccount.setUser(getConfig().getUser());
                    mailAccount.setPass(getConfig().getPassword());
                    mailAccount.setSslEnable(getConfig().isUseSSL());
                    mailAccount.setStarttlsEnable(getConfig().isUseStartTLS());
                }
            }
        }

        return mailAccount;
    }
}
