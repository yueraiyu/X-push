package com.yeay.xpush.pusher;

/**
 * @author yeay
 * @Description 推送器上下文，包含已知推送器
 * @createTime 2022/03/14 18:40:25
 */
public interface PusherContext {
    /**
     * 获取推送器
     *
     * @param config
     * @return
     */
    Pusher configPusher(PusherConfig config);

    /**
     * 更新推送器
     *
     * @param config
     * @return
     */
    Pusher refreshPusher(PusherConfig config);

    /**
     * 通过渠道获取推送器
     *
     * @param channel
     * @return
     */
    Pusher getPusher(String channel);
}
