package com.yeay.xpush.pusher;

import com.yeay.xpush.message.DingMessage;
import com.yeay.xpush.message.DingMessageType;
import com.yeay.xpush.message.DingPushType;
import com.yeay.xpush.pusher.config.DingPushConfig;
import com.yeay.xpush.task.Task;
import com.yeay.xpush.task.TaskExecutor;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author yeay
 * @Description 钉钉推送测试
 * @createTime 2022/03/17 18:03:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class DingPushTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PusherContext context;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private DingPushConfig dingPushConfig;

    @AfterClass
    public static void destroy() throws InterruptedException {
        // 避免测试主线程结束，导致测试线程未正常运行
        Thread.sleep(5000);
    }

    @PostConstruct
    public void initPusher() {
        initDingTalk();
    }

    /**
     * 钉钉机器人推送测试
     */
    @Test
    @Ignore
    public void pushDingTalkRobotTxtMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_DING);

        DingMessage message = new DingMessage();

        message.setPushType(DingPushType.ROBOT);
        message.setMsgType(DingMessageType.TEXT);
        message.setContent("test msg!");
        message.setMobiles(Arrays.asList("replace as target mobile number"));

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 钉钉内部应用推送
     */
    @Test
    @Ignore
    public void pushDingTalkAppTextMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_DING);

        DingMessage message = new DingMessage();

        message.setPushType(DingPushType.WORK);
        message.setMsgType(DingMessageType.TEXT);
        message.setContent("test msg!");
        message.setUserIds(Arrays.asList("replace as target user id"));

        task.setMessage(message);

        executor.execute(task);
    }

    private void initDingTalk() {
        context.configPusher(dingPushConfig);
    }
}
