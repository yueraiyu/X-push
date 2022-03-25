package com.yeay.xpush.message;

/**
 * @author yeay
 * @Description 消息接口
 * @createTime 2022/03/14 10:52:18
 */
public interface Message<T> {

    /**
     * 创建消息
     *
     * @return
     */
    T createMsg();
}
