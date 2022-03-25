package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.chanjar.weixin.mp.bean.subscribe.WxMpSubscribeMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author yeay
 * @Description 微信公众号订阅消息
 * @createTime 2022/03/14 17:36:18
 */
@Setter
@Getter
@ToString
public class WoaSubscribeMessage implements Message<WxMpSubscribeMessage> {
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
     * 点击消息卡片跳转地址，与 miniAppPagePath 只能选择配置一个有效值
     */
    private String templateUrl;

    /**
     * 点击跳转小程序id
     */
    private String miniAppId;

    /**
     * 点击跳转小程序页面地址，与 templateUrl 只能选择配置一个有效值，且必须配置 miniAppId
     */
    private String miniAppPagePath;

    /**
     * 消息内容
     */
    private Map<String, String> data;


    @Override
    public WxMpSubscribeMessage createMsg() {
        WxMpSubscribeMessage wxMessageTemplate = new WxMpSubscribeMessage();

        wxMessageTemplate.setTemplateId(this.templateId);
        wxMessageTemplate.setToUser(this.toUser);
        wxMessageTemplate.setPage(this.templateUrl);

        if (StringUtils.isNotEmpty(miniAppId) && StringUtils.isNotEmpty(miniAppPagePath)) {
            WxMpSubscribeMessage.MiniProgram miniProgram = new WxMpSubscribeMessage
                    .MiniProgram(this.miniAppId, this.miniAppPagePath, false);
            wxMessageTemplate.setMiniProgram(miniProgram);
        }

        wxMessageTemplate.setDataMap(this.data);

        return wxMessageTemplate;
    }
}
