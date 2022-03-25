package com.yeay.xpush.pusher;

import com.yeay.xpush.pusher.config.AbstractWeChatPushConfig;
import com.yeay.xpush.pusher.config.AliSmsPushConfig;
import com.yeay.xpush.pusher.config.DingPushConfig;
import com.yeay.xpush.pusher.config.EmailPushConfig;
import com.yeay.xpush.pusher.config.QqSmsPushConfig;
import com.yeay.xpush.pusher.config.QwPushConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @author yeay
 * @Description TODO
 * @createTime 2022/03/15 15:11:18
 */
@Configuration
@ComponentScan("com.yeay.xpush.*")
@PropertySource(value = {"push-channel.properties"}, encoding = "UTF-8")
public class SpringTestConfig {

    @Value("${push.channel.dingTalk.agentId}")
    private String dingTalkAgentId;

    @Value("${push.channel.dingTalk.appKey}")
    private String dingTalkAppKey;

    @Value("${push.channel.dingTalk.appSecret}")
    private String dingTalkAppSecret;

    @Value("${push.channel.dingTalk.webHook}")
    private String dingTalkWebHook;

    @Value("${push.channel.dingTalk.keyWords}")
    private List<String> dingTalkKeyWords;

    @Value("${push.channel.dingTalk.secret}")
    private String dingTalkSecret;
    @Value("${push.channel.weChat.appId}")
    private String weChatAppId;
    @Value("${push.channel.weChat.appSecret}")
    private String weChatAppSecret;
    @Value("${push.channel.weChat.templateId}")
    private String weChatTemplateId;
    @Value("${push.channel.workWeChat.corpId}")
    private String workWeChatCorpId;
    @Value("${push.channel.workWeChat.agentId}")
    private String workWeChatAgentId;
    @Value("${push.channel.workWeChat.appSecret}")
    private String workWeChatAppSecret;
    @Value("${push.channel.workWeChat.webHook}")
    private String workWeChatWebHook;
    @Value("${push.channel.sms.aliyun.accessKeyId}")
    private String aliAccessKeyId;
    @Value("${push.channel.sms.aliyun.accessKeySecret}")
    private String aliAccessKeySecret;
    @Value("${push.channel.sms.aliyun.signName}")
    private String aliSignName;
    @Value("${push.channel.sms.aliyun.templateCode}")
    private String aliTemplateCode;
    @Value("${push.channel.sms.tencent.appId}")
    private String tencentAppId;
    @Value("${push.channel.sms.tencent.appKey}")
    private String tencentAppKey;
    @Value("${push.channel.sms.tencent.signName}")
    private String tencentSignName;
    @Value("${push.channel.sms.tencent.templateId}")
    private int tencentTemplateCode;
    @Value("${push.channel.email.protocol}")
    private String protocol;
    @Value("${push.channel.email.host}")
    private String host;
    @Value("${push.channel.email.port}")
    private String port;
    @Value("${push.channel.email.from}")
    private String from;
    @Value("${push.channel.email.user}")
    private String user;
    @Value("${push.channel.email.password}")
    private String password;
    @Value("${push.channel.email.useSSL}")
    private boolean useSSL;
    @Value("${push.channel.email.useStartTLS}")
    private boolean useStartTLS;

    @Bean
    public DingPushConfig dingTalkPusherConfig() {
        DingPushConfig dingPushConfig = new DingPushConfig();

        dingPushConfig.setAgentId(dingTalkAgentId);
        dingPushConfig.setAppKey(dingTalkAppKey);
        dingPushConfig.setAppSecret(dingTalkAppSecret);
        dingPushConfig.setWebHook(dingTalkWebHook);
        dingPushConfig.setRobotKeyWords(dingTalkKeyWords);
        dingPushConfig.setRobotSecret(dingTalkSecret);

        return dingPushConfig;
    }

    @Bean
    public AbstractWeChatPushConfig weChatPusherConfig() {
        AbstractWeChatPushConfig weChatPusherConfig = new AbstractWeChatPushConfig();

        weChatPusherConfig.setAppId(weChatAppId);
        weChatPusherConfig.setAppSecret(weChatAppSecret);
        weChatPusherConfig.setTemplateId(weChatTemplateId);

        return weChatPusherConfig;
    }

    @Bean
    public QwPushConfig workWeChatPusherConfig() {
        QwPushConfig qwPushConfig = new QwPushConfig();

        qwPushConfig.setCorpId(workWeChatCorpId);
        qwPushConfig.setAgentId(workWeChatAgentId);
        qwPushConfig.setCorpSecret(workWeChatAppSecret);
        qwPushConfig.setWebHook(workWeChatWebHook);

        return qwPushConfig;
    }

    @Bean
    public AliSmsPushConfig aliYunSmsPusherConfig() {
        AliSmsPushConfig aliSmsPushConfig = new AliSmsPushConfig();

        aliSmsPushConfig.setAccessKeyId(aliAccessKeyId);
        aliSmsPushConfig.setAccessKeySecret(aliAccessKeySecret);
        aliSmsPushConfig.setSignName(aliSignName);
        aliSmsPushConfig.setTemplateCode(aliTemplateCode);

        return aliSmsPushConfig;
    }

    @Bean
    public QqSmsPushConfig tencentSmsPusherConfig() {
        QqSmsPushConfig qqSmsPushConfig = new QqSmsPushConfig();

        qqSmsPushConfig.setAppId(tencentAppId);
        qqSmsPushConfig.setAppKey(tencentAppKey);
        qqSmsPushConfig.setSignName(tencentSignName);
        qqSmsPushConfig.setTemplateId(tencentTemplateCode);

        return qqSmsPushConfig;
    }

    @Bean
    public EmailPushConfig emailPusherConfig() {
        EmailPushConfig emailPushConfig = new EmailPushConfig();

        emailPushConfig.setProtocol(protocol);
        emailPushConfig.setHost(host);
        emailPushConfig.setPort(port);
        emailPushConfig.setFrom(from);
        emailPushConfig.setUser(user);
        emailPushConfig.setPassword(password);
        emailPushConfig.setUseSSL(useSSL);
        emailPushConfig.setUseStartTLS(useStartTLS);

        return emailPushConfig;
    }
}
