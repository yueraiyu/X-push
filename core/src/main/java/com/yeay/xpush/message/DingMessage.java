package com.yeay.xpush.message;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * @author yeay
 * @Description 钉钉 工作号/群机器人 消息
 * @createTime 2022/03/14 14:15:23
 */
@Setter
@Getter
@ToString
public class DingMessage implements Message<OapiMessageCorpconversationAsyncsendV2Request.Msg> {

    /**
     * 钉钉推送类型
     */
    private DingPushType pushType;

    /**
     * 钉钉消息类型
     */
    private DingMessageType msgType;

    /**
     * 钉钉群消息提醒用户
     * eg: ‘@123456’
     */
    private List<String> mobiles;

    /**
     * 消息接收人userId
     */
    private List<String> userIds;

    /**
     * 消息详情
     */
    private String content;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 按钮名称
     */
    private String btnTxt;

    /**
     * 按钮地址
     */
    private String btnUrl;

    @Override
    public OapiMessageCorpconversationAsyncsendV2Request.Msg createMsg() {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        if (Objects.equals(DingMessageType.TEXT, getMsgType())) {
            msg.setMsgtype(DingMessageType.TEXT.getName());

            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent(this.content);
        } else if (Objects.equals(DingMessageType.LINK, getMsgType())) {
            msg.setMsgtype(DingMessageType.LINK.getName());

            msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
            msg.getLink().setTitle(this.title);
            msg.getLink().setText(this.content);
            msg.getLink().setMessageUrl(this.url);
            msg.getLink().setPicUrl(this.picUrl);
        } else if (Objects.equals(DingMessageType.MARKDOWN, getMsgType())) {
            msg.setMsgtype(DingMessageType.MARKDOWN.getName());

            msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
            msg.getMarkdown().setText(this.content);
            msg.getMarkdown().setTitle(this.title);
        } else if (Objects.equals(DingMessageType.ACTION_CARD, getMsgType())) {
            msg.setMsgtype(DingMessageType.ACTION_CARD.getName());

            msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
            msg.getActionCard().setTitle(this.title);
            msg.getActionCard().setMarkdown(this.content);
            msg.getActionCard().setSingleTitle(this.btnTxt);
            msg.getActionCard().setSingleUrl(this.btnUrl);
        }
        return msg;
    }

}
