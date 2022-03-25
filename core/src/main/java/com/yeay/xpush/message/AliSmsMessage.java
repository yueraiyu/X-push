package com.yeay.xpush.message;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.http.MethodType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author yeay
 * @Description 阿里云模板短信
 * @createTime 2022/03/14 10:17:42
 */
@Setter
@Getter
@ToString
public class AliSmsMessage implements Message<SendSmsRequest> {

    /**
     * 短信接收号码
     * <p>
     * 注：上限 1000
     */
    private List<String> phoneNumbers;

    /**
     * 短信模板变量对应值
     */
    private Map<String, String> templateParam;

    @Override
    public SendSmsRequest createMsg() {
        SendSmsRequest request = new SendSmsRequest();

        request.setMethod(MethodType.POST);
        request.setTemplateParam(JSONObject.toJSONString(this.templateParam));
        request.setPhoneNumbers(String.join(",", phoneNumbers));

        return request;
    }
}
