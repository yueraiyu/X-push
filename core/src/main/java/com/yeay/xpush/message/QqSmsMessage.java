package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 腾讯云短信
 * @createTime 2022/03/14 11:56:53
 */
@Setter
@Getter
@ToString
public class QqSmsMessage implements Message<String[]> {

    /**
     * 接收号码
     */
    private String telephoneNum;

    /**
     * 消息参数
     * <p>
     * eg: 模板 <br/>
     * 消息标题:{1}
     * 消息内容:{2}
     * <p>
     * 注: 消息参数顺序必须严格安装配置模板传递
     */
    private String[] templateParams;

    @Override
    public String[] createMsg() {
        return templateParams;
    }
}
