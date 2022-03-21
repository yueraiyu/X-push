package com.yeay.xpush.pusher;

import com.yeay.xpush.message.QwMessage;
import com.yeay.xpush.message.QwMessageType;
import com.yeay.xpush.message.QwPushType;
import com.yeay.xpush.pusher.config.QwPushConfig;
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

/**
 * @author yeay
 * @Description 企业推送测试
 * @createTime 2022/03/17 18:03:10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class WorkWeChatPushTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PusherContext context;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private QwPushConfig qwPushConfig;

    @AfterClass
    public static void destroy() throws InterruptedException {
        // 避免测试主线程结束，导致测试线程未正常运行
        Thread.sleep(5000);
    }

    @PostConstruct
    public void initPusher() {
        initWorkWeChat();
    }

    /**
     * 企业微信群机器人推送文本消息
     */
    @Test
    @Ignore
    public void pushWorkWeChatRobotTextMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_QW);

        QwMessage message = new QwMessage();
        message.setPushType(QwPushType.ROBOT);
        message.setMsgType(QwMessageType.TEXT);
        message.setMsgContent("test message!");
        message.setToUser("replace as target mobile number");

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 企业微信群机器人推送图文消息
     */
    @Test
    @Ignore
    public void pushWorkWeChatRobotNewsMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_QW);

        QwMessage message = new QwMessage();
        message.setPushType(QwPushType.ROBOT);
        message.setMsgType(QwMessageType.NEWS);
        message.setMsgTitle("test");
        message.setDesc("test message!");
        message.setPicUrl("https://cn.bing.com/images/search?view=detailV2&ccid=DK3F7Ryj&id=344B2AA7DB8E037A9D0467FBE589F1B6B7A0C285&thid=OIP.DK3F7Ryj4lxBXdlpyDRvnAHaI4&mediaurl=https%3a%2f%2fassets.puxiang.com%2fuploads%2fphoto%2fimage%2f2341716%2f7c7afaae7bc73519dfab3195bd01a6f7.jpg&exph=1440&expw=1200&q=%e6%9c%ba%e5%99%a8%e4%ba%ba&simid=607992177653068559&FORM=IRPRST&ck=A95A7067C0EAB791BE0F1FE938481A76&selectedIndex=5");
        message.setUrl("https://baike.baidu.com/item/%E6%9C%BA%E5%99%A8%E4%BA%BA/888");

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 企业微信群机器人推送markdown消息
     */
    @Test
    @Ignore
    public void pushWorkWeChatRobotMarkdownMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_QW);

        QwMessage message = new QwMessage();
        message.setPushType(QwPushType.ROBOT);
        message.setMsgType(QwMessageType.MARKDOWN);

        String msg = "### Test\n" +
                "\n" +
                "`test msg!`\n" +
                "\n" +
                "```java\n" +
                "public String sayHello(){\n" +
                "    System.out.print(\"hello！\");\n" +
                "}\n" +
                "```";
        message.setMsgContent(msg);

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 企业微信内部应用文本消息推送
     */
    @Test
    @Ignore
    public void pushWorkWeChatTextMsg() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_QW);

        QwMessage message = new QwMessage();
        message.setPushType(QwPushType.WORK);
        message.setMsgType(QwMessageType.TEXT);
        message.setMsgContent("test msg");
        message.setToUser("replace as target userId");
        message.setToParty("replace as target party");

        task.setMessage(message);

        executor.execute(task);
    }

    private void initWorkWeChat() {
        context.configPusher(qwPushConfig);
    }
}
