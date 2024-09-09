package com.example.helloworld.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author naijia.chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {

    private Integer code;
    private String msg;
    private T data;

    public static final Integer SUCCESS_CODE = 200;

    public ResponseResult(String msg) {
        this.msg = msg;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(SUCCESS_CODE, data);
    }

    public static ResponseResult<String> success(Integer code, String msg) {
        return new ResponseResult<>(code, msg);
    }

    public static <T> ResponseResult<T> fail(Integer code, String msg) {
        return new ResponseResult<T>(code, msg);
    }

    public Boolean success() {
        return this.code == 200;
    }

    public String toJson() {
        return JsonUtil.toJson(this, JsonUtil.SFeature.EMPTY);
    }

    public String toJson(JsonUtil.SFeature... features) {
        return JsonUtil.toJson(this, features);
    }

}
