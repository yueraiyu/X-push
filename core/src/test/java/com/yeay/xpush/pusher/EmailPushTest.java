package com.yeay.xpush.pusher;

import com.yeay.xpush.message.EmailMessage;
import com.yeay.xpush.pusher.config.EmailPushConfig;
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
 * @Description 邮件推送测试
 * @createTime 2022/03/17 18:15:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class EmailPushTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PusherContext context;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private EmailPushConfig emailPushConfig;

    @AfterClass
    public static void destroy() throws InterruptedException {
        // 避免测试主线程结束，导致测试线程未正常运行
        Thread.sleep(5000);
    }

    @PostConstruct
    public void initPusher() {
        initEmail();
    }

    @Test
    @Ignore
    public void pushEmail() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_EMAIL);

        EmailMessage message = new EmailMessage();
        message.setTos(Arrays.asList("replace as target email"));
        message.setCcs(Arrays.asList("replace as target email"));
        message.setBccs(Arrays.asList("replace as target email"));
        message.setTitle("test");
        message.setContent("test msg！");

        task.setMessage(message);

        executor.execute(task);
    }

    private void initEmail() {
        context.configPusher(emailPushConfig);
    }
}
