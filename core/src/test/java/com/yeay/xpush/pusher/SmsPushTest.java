package com.yeay.xpush.pusher;

import com.yeay.xpush.message.AliSmsMessage;
import com.yeay.xpush.message.QqSmsMessage;
import com.yeay.xpush.pusher.config.AliSmsPushConfig;
import com.yeay.xpush.pusher.config.QqSmsPushConfig;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author yeay
 * @Description 短信推送测试
 * @createTime 2022/03/17 18:08:52
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class SmsPushTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PusherContext context;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private AliSmsPushConfig aliSmsPushConfig;

    @Autowired
    private QqSmsPushConfig qqSmsPushConfig;

    @AfterClass
    public static void destroy() throws InterruptedException {
        // 避免测试主线程结束，导致测试线程未正常运行
        Thread.sleep(5000);
    }

    @PostConstruct
    public void initPusher() {
        initAliYunSms();

        initTxYunSms();
    }

    /**
     * 阿里云短信推送测试
     */
    @Test
    @Ignore
    public void pushAliYunSms() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_ALI_SMS);

        AliSmsMessage message = new AliSmsMessage();
        message.setPhoneNumbers(Arrays.asList("replace as target mobile number"));

        Map<String, String> params = new HashMap<>(1);
        params.put("code", "0001");
        message.setTemplateParam(params);

        task.setMessage(message);

        executor.execute(task);
    }

    /**
     * 腾讯云短信推送测试
     */
    @Test
    @Ignore
    public void pushTxYunSms() {
        Task task = applicationContext.getBean(Task.class);

        task.setChannel(PusherConfig.CHANNEL_QQ_SMS);

        QqSmsMessage message = new QqSmsMessage();
        message.setTelephoneNum("replace as target mobile number");

        message.setTemplateParams(new String[]{"0001"});

        task.setMessage(message);

        executor.execute(task);
    }

    private void initAliYunSms() {
        context.configPusher(aliSmsPushConfig);
    }

    private void initTxYunSms() {
        context.configPusher(qqSmsPushConfig);
    }
}
