package com.yeay.xpush.pusher.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.profile.DefaultProfile;
import com.yeay.xpush.message.AliSmsMessage;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.config.AliSmsPushConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author yeay
 * @Description 阿里云短信推送
 * @createTime 2022/03/14 11:00:43
 */
public class AliSmsPusher implements Pusher<AliSmsMessage, AliSmsPushConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliSmsPusher.class);

    private static final String ENDPOINT = "dysmsapi.aliyuncs.com";

    private AliSmsPushConfig config;

    private volatile IAcsClient iAcsClient;

    public AliSmsPusher(AliSmsPushConfig config) {
        setConfig(config);

        initIAcsClient();
    }

    private void initIAcsClient() {
        if (iAcsClient == null) {
            synchronized (AliSmsPusher.class) {
                if (iAcsClient == null) {
                    String accessKeyId = getConfig().getAccessKeyId();
                    String accessKeySecret = getConfig().getAccessKeySecret();

                    // 创建DefaultAcsClient实例并初始化
                    DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

                    // 多个SDK client共享一个连接池，此处设置该连接池的参数，
                    // 比如每个host的最大连接数，超时时间等
                    HttpClientConfig clientConfig = HttpClientConfig.getDefault();
                    clientConfig.setMaxRequestsPerHost(100);
                    clientConfig.setConnectionTimeoutMillis(10000L);

                    profile.setHttpClientConfig(clientConfig);
                    iAcsClient = new DefaultAcsClient(profile);
                }
            }
        }
    }

    @Override
    public AliSmsPushConfig getConfig() {
        return this.config;
    }

    @Override
    public void setConfig(AliSmsPushConfig config) {
        this.config = config;
    }

    @Override
    public PushResponse send(AliSmsMessage msg) {
        PushResponse pushResponse = new PushResponse();

        try {
            SendSmsRequest sendSmsRequest = msg.createMsg();
            sendSmsRequest.setEndpoint(ENDPOINT);

            sendSmsRequest.setSignName(getConfig().getSignName());
            sendSmsRequest.setTemplateCode(getConfig().getTemplateCode());

            SendSmsResponse response = iAcsClient.getAcsResponse(sendSmsRequest);
            if (Objects.equals(PushResponse.RESPONSE_IS_SUCCESS, response.getCode())) {
                pushResponse.setSuccess(true);
            } else {
                LOGGER.warn("push message fail! {}", response.getMessage());
                pushResponse.setSuccess(false);
                pushResponse.setMessage(response.getMessage());
                pushResponse.setCode(response.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("push message fail!", e);
            pushResponse.setSuccess(false);
            pushResponse.setMessage(e.getMessage());
        }

        return pushResponse;
    }
}
