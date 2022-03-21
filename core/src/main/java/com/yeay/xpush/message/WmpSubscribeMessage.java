package com.yeay.xpush.message;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author yeay
 * @Description 微信小程序订阅消息
 * @createTime 2022/03/14 18:06:10
 */
@Setter
@Getter
@ToString
public class WmpSubscribeMessage implements Message<WxMaSubscribeMessage> {

    /**
     * 接收人
     * <p>
     * 注： 公众号用户 openId
     */
    private String toUser;

    /**
     * 消息模板 id
     */
    private String templateId;

    /**
     * 点击消息卡片跳转地址
     */
    private String templateUrl;

    /**
     * 消息内容
     */
    private List<TemplateParam> templateParamList;

    @Override
    public WxMaSubscribeMessage createMsg() {
        WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();

        wxMaSubscribeMessage.setTemplateId(this.templateId);
        wxMaSubscribeMessage.setPage(this.templateUrl);
        wxMaSubscribeMessage.setToUser(this.toUser);

        WxMaSubscribeMessage.MsgData wxMaSubscribeData;
        for (TemplateParam templateParam : templateParamList) {
            wxMaSubscribeData = new WxMaSubscribeMessage.MsgData();
            wxMaSubscribeData.setName(templateParam.getName());
            wxMaSubscribeData.setValue(templateParam.getValue());
            wxMaSubscribeMessage.addData(wxMaSubscribeData);
        }

        return wxMaSubscribeMessage;
    }
}
