package com.yeay.xpush.pusher.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yeay
 * @Description 推送结果
 * @createTime 2022/03/14 10:52:34
 */
@Getter
@Setter
@ToString
public class PushResponse {

    public static final String RESPONSE_IS_SUCCESS = "OK";

    private boolean success = false;

    private String message;

    private String code;
}
