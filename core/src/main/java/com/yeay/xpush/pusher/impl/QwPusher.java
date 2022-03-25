package com.yeay.xpush.pusher.impl;

import com.google.common.collect.Lists;
import com.yeay.xpush.message.QwMessage;
import com.yeay.xpush.message.QwMessageType;
import com.yeay.xpush.message.QwPushType;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.QwPushConfig;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpGroupRobotServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpServiceApacheHttpClientImpl;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author yeay
 * @Description 企业微信消息推送器
 * @createTime 2022/03/14 15:18:52
 */
public class QwPusher implements Pusher<QwMessage, QwPushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QwPusher.class);

    private QwPushConfig config;

    private volatile WxCpDefaultConfigImpl wxCpConfigStorage;

    private volatile WxCpService wxCpService;

    private WxCpGroupRobotServiceImpl robotService;

    public QwPusher(QwPushConfig config) {
        setConfig(config);

        initWxCpConfigStorage();
        initWxCpService();
    }

    @Override
    public QwPushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(QwPushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(QwMessage msg) {
        if (Objects.equals(msg.getPushType(), QwPushType.WORK)) {
            return sendWorkMsg(msg);
        } else {
            return sendRobotMsg(msg);
        }
    }

    private WxCpDefaultConfigImpl initWxCpConfigStorage() {
        WxCpDefaultConfigImpl configStorage = new WxCpDefaultConfigImpl();

        if (StringUtils.isNotEmpty(getConfig().getCorpId())) {
            configStorage.setCorpId(getConfig().getCorpId());
        }

        if (StringUtils.isNotEmpty(getConfig().getAgentId())) {
            configStorage.setAgentId(Integer.valueOf(getConfig().getAgentId()));
        }

        if (StringUtils.isNotEmpty(getConfig().getCorpSecret())) {
            configStorage.setCorpSecret(getConfig().getCorpSecret());
        }

        if (StringUtils.isNotEmpty(getConfig().getWebHook())) {
            configStorage.setWebhookKey(getConfig().getWebHook());
        }

        DefaultApacheHttpClientBuilder clientBuilder = DefaultApacheHttpClientBuilder.get();
        //从连接池获取链接的超时时间(单位ms)
        clientBuilder.setConnectionRequestTimeout(10000);
        //建立链接的超时时间(单位ms)
        clientBuilder.setConnectionTimeout(5000);
        //连接池socket超时时间(单位ms)
        clientBuilder.setSoTimeout(5000);
        //空闲链接的超时时间(单位ms)
        clientBuilder.setIdleConnTimeout(60000);
        //空闲链接的检测周期(单位ms)
        clientBuilder.setCheckWaitTime(60000);
        //每路最大连接数
        clientBuilder.setMaxConnPerHost(100);
        //连接池最大连接数
        clientBuilder.setMaxTotalConn(100);

        configStorage.setApacheHttpClientBuilder(clientBuilder);

        return configStorage;
    }

    private void initWxCpService() {
        if (wxCpConfigStorage == null) {
            synchronized (QwPusher.class) {
                if (wxCpConfigStorage == null) {
                    wxCpConfigStorage = initWxCpConfigStorage();
                }
            }
        }

        if (wxCpService == null && wxCpConfigStorage != null) {
            synchronized (QwPusher.class) {
                if (wxCpService == null && wxCpConfigStorage != null) {
                    wxCpService = new WxCpServiceApacheHttpClientImpl();
                    wxCpService.setWxCpConfigStorage(wxCpConfigStorage);

                    robotService = new WxCpGroupRobotServiceImpl(wxCpService);
                }
            }
        }
    }

    private PushResponse sendRobotMsg(QwMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            if (Objects.equals(QwMessageType.NEWS, msg.getMsgType())) {
                NewArticle article = new NewArticle();

                article.setTitle(msg.getMsgTitle());
                article.setPicUrl(msg.getPicUrl());
                article.setDescription(msg.getDesc());
                article.setUrl(msg.getUrl());

                robotService.sendNews(getConfig().getWebHook(), Lists.newArrayList(article));
            } else if (Objects.equals(QwMessageType.TEXT, msg.getMsgType())) {
                robotService.sendText(getConfig().getWebHook(), msg.getMsgContent(), null, Lists.newArrayList(msg.getToUser()));
            } else if (Objects.equals(QwMessageType.MARKDOWN, msg.getMsgType())) {
                robotService.sendMarkdown(getConfig().getWebHook(), msg.getMsgContent());
            }

            pushResponse.setSuccess(true);
            return pushResponse;
        } catch (WxErrorException e) {
            LOGGER.error("push work wechat robot msg fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }
    }

    private PushResponse sendWorkMsg(QwMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            WxCpMessage wxCpMessage = msg.createMsg();
            wxCpMessage.setAgentId(Integer.valueOf(getConfig().getAgentId()));

            WxCpMessageSendResult wxCpMessageSendResult = wxCpService.getMessageService().send(wxCpMessage);
            if (wxCpMessageSendResult.getErrCode() != 0 || StringUtils.isNoneEmpty(wxCpMessageSendResult.getInvalidUser())) {
                pushResponse.setSuccess(false);
                pushResponse.setMessage(wxCpMessageSendResult.toString());
                return pushResponse;
            }
        } catch (Exception e) {
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }

        pushResponse.setSuccess(true);
        return pushResponse;
    }
}
