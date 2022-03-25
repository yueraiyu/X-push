package com.yeay.xpush.pusher.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.yeay.xpush.message.WmpSubscribeMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.WmpSubscribePushConfig;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yeay
 * @Description 微信小程序订阅消息推送器
 * @createTime 2022/03/14 18:09:10
 */
public class WmpSubscribePusher implements Pusher<WmpSubscribeMessage, WmpSubscribePushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmpSubscribePusher.class);

    private volatile WxMaService wxMaService;

    private volatile WxMaDefaultConfigImpl wxMaConfigStorage;

    private WmpSubscribePushConfig config;

    public WmpSubscribePusher(WmpSubscribePushConfig config) {
        setConfig(config);

        initWxMaConfigStorage();
        initWxMaService();
    }

    @Override
    public WmpSubscribePushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(WmpSubscribePushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(WmpSubscribeMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            WxMaSubscribeMessage wxMaSubscribeMessage = msg.createMsg();
            if (StringUtils.isNotEmpty(getConfig().getTemplateId())
                    && StringUtils.isEmpty(wxMaSubscribeMessage.getTemplateId())) {
                wxMaSubscribeMessage.setTemplateId(getConfig().getTemplateId());
            }

            wxMaService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (Exception e) {
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
            return pushResponse;
        }

        pushResponse.setSuccess(true);
        return pushResponse;
    }

    private WxMaService initWxMaService() {
        if (wxMaService == null) {
            synchronized (WmpSubscribePusher.class) {
                if (wxMaService == null) {
                    wxMaService = new WxMaServiceImpl();
                }
            }
        }
        if (wxMaConfigStorage == null) {
            synchronized (WmpSubscribePusher.class) {
                if (wxMaConfigStorage == null) {
                    wxMaConfigStorage = initWxMaConfigStorage();
                    if (wxMaConfigStorage != null) {
                        wxMaService.setWxMaConfig(wxMaConfigStorage);
                    }
                }
            }
        }
        return wxMaService;
    }

    private WxMaDefaultConfigImpl initWxMaConfigStorage() {
        WxMaDefaultConfigImpl configStorage = new WxMaDefaultConfigImpl();
        configStorage.setAppid(getConfig().getMiniAppAppId());
        configStorage.setSecret(getConfig().getMiniAppAppSecret());
        configStorage.setToken(getConfig().getMiniAppToken());
        configStorage.setAesKey(getConfig().getMiniAppAesKey());
        configStorage.setMsgDataFormat("JSON");

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
}
