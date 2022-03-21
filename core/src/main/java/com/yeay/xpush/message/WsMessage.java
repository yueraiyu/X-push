package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;

import java.util.Objects;

/**
 * @author yeay
 * @Description 微信客服消息
 * @createTime 2022/03/14 16:30:11
 */
@Setter
@Getter
@ToString
public class WsMessage implements Message<WxMpKefuMessage> {

    /**
     * 接收人
     * <p>
     * 注： 公众号用户 openId
     */
    private String toUser;

    private WsMessageType msgType;

    /**
     * 消息标题，仅文本消息设置
     */
    private String title;

    /**
     * 消息详情
     */
    private String content;

    /**
     * 图片地址，仅文本消息设置
     */
    private String picUrl;

    /**
     * 消息描述信息，仅文本消息设置
     */
    private String desc;

    /**
     * 消息跳转地址，仅文本消息设置
     */
    private String url;

    /**
     * 卡片消息点击跳转地址
     */
    private String pagePath;

    /**
     * 视频消息缩略图的媒体id
     */
    private String thumbMediaId;

    @Override
    public WxMpKefuMessage createMsg() {
        WxMpKefuMessage keFuMessage = null;

        if (Objects.equals(WsMessageType.NEWS, msgType)) {
            WxMpKefuMessage.WxArticle article = new WxMpKefuMessage.WxArticle();

            article.setTitle(this.title);
            article.setPicUrl(this.picUrl);
            article.setDescription(this.desc);
            article.setUrl(this.url);

            keFuMessage = WxMpKefuMessage.NEWS()
                    .addArticle(article)
                    .toUser(this.toUser)
                    .build();
        } else if (Objects.equals(WsMessageType.TEXT, msgType)) {
            keFuMessage = WxMpKefuMessage.TEXT()
                    .content(this.content)
                    .toUser(this.toUser)
                    .build();
        } else if (Objects.equals(WsMessageType.MINI_PROGRAM_CARD, msgType)) {
            keFuMessage = WxMpKefuMessage.MINIPROGRAMPAGE()
                    .title(this.title)
                    .pagePath(this.pagePath)
                    .thumbMediaId(this.thumbMediaId)
                    .toUser(this.toUser)
                    .build();
        }

        return keFuMessage;
    }
}
