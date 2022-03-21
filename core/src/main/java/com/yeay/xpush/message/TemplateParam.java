package com.yeay.xpush.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yeay
 * @Description 模板数据
 * @createTime 2022/03/14 10:17:42
 */
@Getter
@Setter
public class TemplateParam implements Serializable {

    /**
     * 模板字段名
     */
    private String name;

    /**
     * 模板字段值
     */
    private String value;

    /**
     * 字段值显示颜色
     */
    private String color;
}
