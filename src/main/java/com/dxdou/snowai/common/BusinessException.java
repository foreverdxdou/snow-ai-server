package com.dxdou.snowai.common;

/**
 * 业务异常
 *
 * @author foreverdxdou
 */
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 构造方法
     *
     * @param message 消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法
     *
     * @param message 消息
     * @param cause   原因
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 消息
     * @param cause   原因
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}