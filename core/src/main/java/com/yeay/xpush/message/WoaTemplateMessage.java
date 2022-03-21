package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yeay
 * @Description 微信公众号模板消息
 * @createTime 2022/03/14 17:50:09
 */
@Setter
@Getter
@ToString
public class WoaTemplateMessage implements Message<WxMpTemplateMessage> {

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
     * 跳转小程序 appId
     */
    private String miniAppId;

    /**
     * 跳转小程序 pagePath
     */
    private String miniAppPagePath;

    /**
     * 消息内容
     */
    private List<TemplateParam> templateParamList;

    @Override
    public WxMpTemplateMessage createMsg() {
        WxMpTemplateMessage wxMessageTemplate = new WxMpTemplateMessage();

        wxMessageTemplate.setTemplateId(this.templateId);
        wxMessageTemplate.setToUser(this.toUser);
        wxMessageTemplate.setUrl(this.templateUrl);

        if (StringUtils.isNotEmpty(miniAppId) && StringUtils.isNotEmpty(miniAppPagePath)) {
            WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram(this.miniAppId, this.miniAppPagePath, false);
            wxMessageTemplate.setMiniProgram(miniProgram);
        }

        WxMpTemplateData wxMpTemplateData;
        for (TemplateParam templateParam : templateParamList) {
            wxMpTemplateData = new WxMpTemplateData();
            wxMpTemplateData.setName(templateParam.getName());
            wxMpTemplateData.setValue(templateParam.getValue());
            wxMpTemplateData.setColor(templateParam.getColor());
            wxMessageTemplate.addData(wxMpTemplateData);
        }

        return wxMessageTemplate;
    }
}
