package com.yeay.xpush.task;

import cn.hutool.core.lang.Assert;
import com.yeay.xpush.message.Message;
import com.yeay.xpush.pusher.Pusher;
import com.yeay.xpush.pusher.PusherConfig;
import com.yeay.xpush.pusher.PusherContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author yeay
 * @Description 推送任务
 * @createTime 2022/03/15 10:32:25
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    @Autowired
    private PusherContext pusherContext;

    /**
     * 渠道标识
     *
     * @see PusherConfig "CHANNEL_*"
     */
    private String channel;

    private Message message;

    @Override
    public void run() {
        Assert.notNull(pusherContext, "PusherContext can't be NULL!");
        Assert.notEmpty(channel, "Push channel is empty!");

        Pusher pusher = pusherContext.getPusher(channel);
        Assert.notNull(pusher, "Not found %s pusher!", channel);

        Assert.notNull(message, "Message can't be NULL!");
        pusher.send(message);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
