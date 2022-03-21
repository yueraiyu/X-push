package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.List;

/**
 * @author yeay
 * @Description 邮件消息
 * @createTime 2022/03/14 18:19:47
 */
@Setter
@Getter
@ToString
public class EmailMessage implements Message<EmailMessage> {

    /**
     * 接收人
     */
    private List<String> tos;

    /**
     * 抄送人
     */
    private List<String> ccs;

    /**
     * 密送人
     */
    private List<String> bccs;

    /**
     * 邮件附件
     */
    private List<File> files;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件内容
     */
    private String content;

    @Override
    public EmailMessage createMsg() {
        return this;
    }
}
