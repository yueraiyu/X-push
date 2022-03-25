package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;

import java.util.Objects;

/**
 * @author yeay
 * @Description 企业微信 工作号/群机器人 消息
 * @createTime 2022/03/14 15:16:35
 */
@Setter
@Getter
@ToString
public class QwMessage implements Message<WxCpMessage> {

    /**
     * 企业微信推送类型
     */
    private QwPushType pushType;

    /**
     * 企业微信消息类型
     */
    private QwMessageType msgType;

    /**
     * 接收人
     * <p>
     * 注： 公众号用户 openId
     */
    private String toUser;

    /**
     * 接收消息的部门
     */
    private String toParty;

    /**
     * 接收消息的标签
     */
    private String toTag;

    /**
     * 描述, 仅图文消息与卡片消息设置
     */
    private String desc;

    /**
     * 点击后跳转的链接，仅图文消息与卡片消息设置
     */
    private String url;

    /**
     * 标题，仅图文消息与卡片消息设置
     */
    private String msgTitle;

    /**
     * 图片地址，仅图文消息设置
     */
    private String picUrl;

    /**
     * 按钮名称，仅卡片消息设置
     */
    private String btnTxt;

    /**
     * 消息详情
     */
    private String msgContent;

    @Override
    public WxCpMessage createMsg() {
        WxCpMessage wxCpMessage = null;

        if (Objects.equals(QwPushType.WORK, this.pushType)) {
            wxCpMessage = createWorkMsg();
        } else if (Objects.equals(QwPushType.ROBOT, this.pushType)) {
            throw new UnsupportedOperationException("Can't create robot message!");
        }

        return wxCpMessage;
    }

    private WxCpMessage createWorkMsg() {
        WxCpMessage wxCpMessage = null;

        if (Objects.equals(QwMessageType.NEWS, this.msgType)) {
            NewArticle article = new NewArticle();

            article.setTitle(this.msgTitle);
            article.setPicUrl(this.picUrl);
            article.setDescription(this.desc);
            article.setUrl(this.url);

            wxCpMessage = WxCpMessage.NEWS()
                    .toUser(this.toUser)
                    .toParty(this.toParty)
                    .toTag(this.toTag)
                    .addArticle(article)
                    .build();
        } else if (Objects.equals(QwMessageType.TEXT, this.msgType)) {
            wxCpMessage = WxCpMessage.TEXT()
                    .toUser(this.toUser)
                    .toParty(this.toParty)
                    .toTag(this.toTag)
                    .content(this.msgContent)
                    .build();
        } else if (Objects.equals(QwMessageType.MARKDOWN, this.msgType)) {
            wxCpMessage = WxCpMessage.MARKDOWN()
                    .toUser(this.toUser)
                    .toParty(this.toParty)
                    .toTag(this.toTag)
                    .content(this.msgContent)
                    .build();
        } else if (Objects.equals(QwMessageType.TEXT_CARD, this.msgType)) {
            wxCpMessage = WxCpMessage.TEXTCARD()
                    .toUser(this.toUser)
                    .toParty(this.toParty)
                    .toTag(this.toTag)
                    .title(this.msgTitle)
                    .description(this.desc)
                    .url(this.url)
                    .btnTxt(this.btnTxt)
                    .build();
        }

        return wxCpMessage;
    }
}
