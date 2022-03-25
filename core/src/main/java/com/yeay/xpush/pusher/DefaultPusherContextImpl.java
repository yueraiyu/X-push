package com.yeay.xpush.pusher;

import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yeay
 * @Description 默认消息推送器上下文
 * @createTime 2022/03/14 18:42:21
 */
@Component
public class DefaultPusherContextImpl implements PusherContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPusherContextImpl.class);

    private Map<String, Pusher> pusherMap = new ConcurrentHashMap<>(10);

    @Override
    public Pusher configPusher(PusherConfig config) {
        try {
            if (pusherMap.containsKey(config.getChannel())) {
                return pusherMap.get(config.getChannel());
            }

            return pusherMap.put(config.getChannel(), config.configPusher());
        } catch (IOReactorException e) {
            LOGGER.error("Can't config {} pusher!", config.getChannel(), e);
            return null;
        }
    }

    @Override
    public Pusher refreshPusher(PusherConfig config) {
        try {
            if (pusherMap.containsKey(config.getChannel())) {
                return pusherMap.replace(config.getChannel(), config.configPusher());
            }

            return pusherMap.put(config.getChannel(), config.configPusher());
        } catch (IOReactorException e) {
            LOGGER.error("Can't config {} pusher!", config.getChannel(), e);
            return null;
        }
    }

    @Override
    public Pusher getPusher(String channel) {
        return pusherMap.get(channel);
    }
}
