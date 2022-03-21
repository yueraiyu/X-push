package com.yeay.xpush.pusher.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 微信推送公共配置
 * @createTime 2022/03/14 16:53:38
 */
@Setter
@Getter
@ToString
public class AbstractWeChatPushConfig {

    /**
     * 应用凭证
     */
    private String appId;

    /**
     * 应用凭证密钥
     */
    private String appSecret;

    private String token;

    /**
     * 消息加密密钥
     */
    private String aesKey;

    /**
     * 消息模板id
     */
    private String templateId;
}
