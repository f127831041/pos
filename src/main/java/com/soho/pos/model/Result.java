package com.soho.pos.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class Result<T> {
    /**
     * 業務錯誤碼
     */
    private Integer code;
    /**
     * 信息描述
     */
    private String message;
    /**
     * 返回參數
     */
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    /**
     * 業務成功返回業務代碼和描述信息
     */
    public static Result<Void> success() {
        return new Result<Void>(HttpStatus.OK.value(), "ok", null);
    }

    /**
     * 業務成功返回業務代碼,描述和返回的參數
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(HttpStatus.OK.value(), "ok", data);
    }

    /**
     * 業務成功返回業務代碼,描述和返回的參數
     */
    public static <T> Result<T> success(HttpStatus status, T data) {
        if (status == null) {
            return success(data);
        }
        return new Result<T>(status.value(), "ok", data);
    }

    /**
     * 業務異常返回業務代碼和描述信息
     */
    public static <T> Result<T> failure() {
        return new Result<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null);
    }

    /**
     * 業務異常返回業務代碼,描述和返回的參數
     */
    public static <T> Result<T> failure(HttpStatus status, String message) {
        return failure(status, message, null);
    }

    /**
     * 業務異常返回業務代碼,描述和返回的參數
     */
    public static <T> Result<T> failure(HttpStatus status, String message, T data) {
        if (status == null) {
            return failure();
        }
        return new Result<T>(status.value(), message, data);
    }
}
