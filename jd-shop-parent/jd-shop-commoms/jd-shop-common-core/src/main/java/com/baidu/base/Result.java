package com.baidu.base;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @version 1.0
 * @author:王双全
 * @date: 2020-30-27 22:30
 */
@Data
@NoArgsConstructor
public class Result<T> {

    private Integer code;//返回码

    private String message;//返回消息

    private T data;//返回数据

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = (T) data;
    }
}
