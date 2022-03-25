package com.yeay.xpush.pusher;


import com.yeay.xpush.message.Message;
import com.yeay.xpush.pusher.impl.PushResponse;

/**
 * @author yeay
 * @Description 推送器接口
 * @createTime 2022/03/14 10:52:34
 */
public interface Pusher<M extends Message, C extends PusherConfig> {
    /**
     * 获取推送配置
     *
     * @return
     */
    C getConfig();

    /**
     * 设置配置
     *
     * @param config
     */
    void setConfig(C config);

    /**
     * 发送消息
     *
     * @param msg 消息数据
     * @return
     */
    PushResponse send(M msg);
}
