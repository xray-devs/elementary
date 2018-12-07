package com.elementary.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Mr丶s
 * @ClassName 业务异常类
 * @Version   V1.0
 * @Date   2018/8/29 18:01
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ElementaryRuntimeException extends RuntimeException {

    /**
     * 异常编码
     */
    private Object code;
    /**
     * 中文提示语
     */
    private String cnMessage;


    public ElementaryRuntimeException(String message) {
        super(message);
    }

    public ElementaryRuntimeException(String message, Object code) {
        super(message);
        this.code = code;
    }

    public ElementaryRuntimeException(String cnMessage, String message, Object code) {
        this(message, code);
        this.cnMessage = cnMessage;
    }
}




