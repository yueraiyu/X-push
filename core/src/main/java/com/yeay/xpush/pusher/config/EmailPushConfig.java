package com.yeay.xpush.pusher.config;

import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.impl.EmailPusher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 邮件推送配置
 * @createTime 2022/03/14 18:24:07
 */
@Setter
@Getter
@ToString
public class EmailPushConfig implements PusherConfig {

    /**
     * 邮件服务协议
     * <p>
     * 注： 现阶段使用 hutool，仅支持smtp协议
     */
    private String protocol;

    /**
     * 邮件服务域名
     */
    private String host;

    /**
     * 邮件服务端口
     */
    private String port;

    /**
     * 邮件发送人邮箱
     */
    private String from;

    /**
     * 邮箱认证，用户邮箱
     */
    private String user;

    /**
     * 邮箱认证，用户邮箱密码
     */
    private String password;

    /**
     * 是否使用 SSL 协议，SMTP 必须使用
     */
    private boolean useSSL;

    /**
     * 是否使用 StartTLS
     */
    private boolean useStartTLS;

    @Override
    public Pusher configPusher() {
        return new EmailPusher(this);
    }

    @Override
    public String getChannel() {
        return CHANNEL_EMAIL;
    }
}
