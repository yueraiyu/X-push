package com.yeay.xpush.pusher;

import org.apache.http.nio.reactor.IOReactorException;

/**
 * @author yeay
 * @Description 推送配置接口
 * @createTime 2022/03/14 11:01:10
 */
public interface PusherConfig {

    /**
     * 渠道-阿里云短信
     */
    String CHANNEL_ALI_SMS = "ALI_SMS";

    /**
     * 渠道-钉钉
     */
    String CHANNEL_DING = "DING";

    /**
     * 渠道-邮件
     */
    String CHANNEL_EMAIL = "EMAIL";

    /**
     * 渠道-腾讯云短信
     */
    String CHANNEL_QQ_SMS = "QQ_SMS";

    /**
     * 渠道-微信小程序订阅
     */
    String CHANNEL_WMP_SUBSCRIBE = "WMP_SUBSCRIBE";

    /**
     * 渠道-微信公众号订阅
     */
    String CHANNEL_WOA_SUBSCRIBE = "WOA_SUBSCRIBE";

    /**
     * 渠道-微信公众号模板
     */
    String CHANNEL_WOA_TEMPLATE = "WOA_TEMPLATE";

    /**
     * 渠道-微信客服
     */
    String CHANNEL_WS = "WS";

    /**
     * 渠道-企业微信
     */
    String CHANNEL_QW = "QW";

    /**
     * 配置推送器
     *
     * @return
     * @throws IOReactorException
     */
    Pusher configPusher() throws IOReactorException;

    /**
     * 获取渠道名称
     *
     * @return
     */
    String getChannel();
}
