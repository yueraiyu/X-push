package com.yeay.xpush.pusher.impl;

import com.yeay.xpush.pusher.config.AbstractWeChatPushConfig;
import me.chanjar.weixin.common.util.http.apache.DefaultApacheHttpClientBuilder;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yeay
 * @Description 微信推送
 * @createTime 2022/03/14 16:48:34
 */
public abstract class AbstractWeChatPusher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWeChatPusher.class);

    protected volatile WxMpDefaultConfigImpl wxMpConfigStorage;

    protected volatile WxMpService wxMpService;

    protected volatile CloseableHttpAsyncClient closeableHttpAsyncClient;

    protected AbstractWeChatPushConfig weChatPusherConfig;

    public AbstractWeChatPusher(AbstractWeChatPushConfig weChatPusherConfig) throws IOReactorException {
        setWeChatPusherConfig(weChatPusherConfig);

        initWxMpConfigStorage();
        initWxMpService();
        initCloseableHttpAsyncClient();
    }

    public AbstractWeChatPushConfig getWeChatPusherConfig() {
        return weChatPusherConfig;
    }

    public void setWeChatPusherConfig(AbstractWeChatPushConfig weChatPusherConfig) {
        this.weChatPusherConfig = weChatPusherConfig;
    }

    protected WxMpDefaultConfigImpl initWxMpConfigStorage() {
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(getWeChatPusherConfig().getAppId());
        configStorage.setSecret(getWeChatPusherConfig().getAppSecret());
        configStorage.setToken(getWeChatPusherConfig().getToken());
        configStorage.setAesKey(getWeChatPusherConfig().getAesKey());


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
        clientBuilder.setCheckWaitTime(3000);
        //每路最大连接数
        clientBuilder.setMaxConnPerHost(100);
        //连接池最大连接数
        clientBuilder.setMaxTotalConn(100);

        configStorage.setApacheHttpClientBuilder(clientBuilder);
        return configStorage;
    }

    protected WxMpService initWxMpService() {
        if (wxMpConfigStorage == null) {
            synchronized (AbstractWeChatPusher.class) {
                if (wxMpConfigStorage == null) {
                    wxMpConfigStorage = initWxMpConfigStorage();
                }
            }
        }
        if (wxMpService == null && wxMpConfigStorage != null) {
            synchronized (AbstractWeChatPusher.class) {
                if (wxMpService == null && wxMpConfigStorage != null) {
                    wxMpService = new WxMpServiceImpl();
                    wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
                }
            }
        }
        return wxMpService;
    }

    protected CloseableHttpAsyncClient initCloseableHttpAsyncClient() throws IOReactorException {
        if (closeableHttpAsyncClient == null) {
            synchronized (AbstractWeChatPusher.class) {
                if (closeableHttpAsyncClient == null) {
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectTimeout(-1)
                            .setSocketTimeout(-1)
                            .setConnectionRequestTimeout(-1)
                            .build();

                    //配置io线程
                    IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                            setIoThreadCount(Runtime.getRuntime().availableProcessors())
                            .setSoKeepAlive(true).setConnectTimeout(-1).setSoTimeout(-1)
                            .build();
                    //设置连接池大小
                    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
                    PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
                    //最大连接数
                    connManager.setMaxTotal(5000);
                    //per route最大连接数
                    connManager.setDefaultMaxPerRoute(5000);

                    closeableHttpAsyncClient = HttpAsyncClients.custom().
                            setConnectionManager(connManager)
                            .setDefaultRequestConfig(requestConfig)
                            .build();

                    closeableHttpAsyncClient.start();
                }
            }
        }

        return closeableHttpAsyncClient;
    }
}
