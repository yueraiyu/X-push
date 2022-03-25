package com.yeay.xpush.pusher;

import com.yeay.xpush.message.TemplateParam;
import com.yeay.xpush.message.WoaSubscribeMessage;
import com.yeay.xpush.message.WoaTemplateMessage;
import com.yeay.xpush.message.WsMessage;
import com.yeay.xpush.message.WsMessageType;
import com.yeay.xpush.pusher.config.AbstractWeChatPushConfig;
import com.yeay.xpush.pusher.config.WoaTemplatePushConfig;
import com.yeay.xpush.pusher.config.WoaSubscribePushConfig;
import com.yeay.xpush.pusher.config.WsPushConfig;
import com.yeay.xpush.task.Task;
import com.yeay.xpush.task.TaskExecutor;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yeay
 * @Description 微信公众号推送测试
 * @createTime 2022/03/17 18:11:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class WeChatOfficialAccountsPushTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PusherContext context;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private AbstractWeChatPushConfig weChatPusherConfig;

    @Value("${push.channel.weChat.toUser}")
    private String toUser;

    @AfterClass
    public static void destroy() throws InterruptedException {
        // 避免测试主线程结束，导致测试线程未正常运行
        Thread.sleep(5000);
    }

    @PostConstruct
    public void initPusher() {
        initWeChat();
    }

    /**
     * 推送微信公众号订阅通知
     * <p>
     * 注：测试不可用，需要认证
     */
    @Test
    @Ignore
    public void pushWxOasMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_WOA_SUBSCRIBE);

        WoaSubscribeMessage message = new WoaSubscribeMessage();
        message.setToUser(toUser);

        Map<String, String> data = new HashMap<>(1);
        data.put("msg", "test message！");
        message.setData(data);

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 推送微信公众号模板消息
     */
    @Test
    @Ignore
    public void pushWxOatMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_WOA_TEMPLATE);

        WoaTemplateMessage message = new WoaTemplateMessage();
        message.setToUser(toUser);

        TemplateParam templateParam = new TemplateParam();
        templateParam.setName("message");
        templateParam.setValue("test message!");
        templateParam.setColor("#0000FF");

        message.setTemplateParamList(Arrays.asList(templateParam));

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 推送微信公众号服务消息
     */
    @Test
    @Ignore
    public void pushWxServiceMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_WS);

        WsMessage message = new WsMessage();
        message.setMsgType(WsMessageType.TEXT);
        message.setToUser(toUser);
        message.setContent("test message!");

        task.setMessage(message);

        executor.execute(task);
    }

    private void initWeChat() {
        WoaSubscribePushConfig ofsConfig = new WoaSubscribePushConfig();
        ofsConfig.setAppId(weChatPusherConfig.getAppId());
        ofsConfig.setAppSecret(weChatPusherConfig.getAppSecret());
        ofsConfig.setTemplateId(weChatPusherConfig.getTemplateId());

        WoaTemplatePushConfig oftConfig = new WoaTemplatePushConfig();
        oftConfig.setAppId(weChatPusherConfig.getAppId());
        oftConfig.setAppSecret(weChatPusherConfig.getAppSecret());
        oftConfig.setTemplateId(weChatPusherConfig.getTemplateId());

//        WeChatMiniProgramSubscribePusherConfig mpsConfig = new WeChatMiniProgramSubscribePusherConfig();
//        mpsConfig.setAppId(weChatPusherConfig.getAppId());
//        mpsConfig.setAppSecret(weChatPusherConfig.getAppSecret());
//        mpsConfig.setTemplateId(weChatPusherConfig.getTemplateId());

        WsPushConfig sconfig = new WsPushConfig();
        sconfig.setAppId(weChatPusherConfig.getAppId());
        sconfig.setAppSecret(weChatPusherConfig.getAppSecret());
        sconfig.setTemplateId(weChatPusherConfig.getTemplateId());

        context.configPusher(ofsConfig);
        context.configPusher(oftConfig);
        context.configPusher(sconfig);
    }
}
