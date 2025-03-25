package com.dxdou.snowai.common;

import lombok.Data;

/**
 * 通用返回类
 *
 * @author foreverdxdou
 */
@Data
public class R<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功
     *
     * @param data 数据
     * @return 返回结果
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param data    数据
     * @return 返回结果
     */
    public static <T> R<T> ok(String message, T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    /**
     * 失败
     *
     * @param code    状态码
     * @param message 消息
     * @return 返回结果
     */
    public static <T> R<T> error(Integer code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    /**
     * 失败
     *
     * @param message 消息
     * @return 返回结果
     */
    public static <T> R<T> error(String message) {
        return error(500, message);
    }
} 