package com.example.communityserver.utils.web;

import lombok.*;

import java.util.List;

/**
 * <p>
 * 数据返回
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 通用成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 通用成功响应（不带数据）
    public static Result<Void> success() {
        return new Result<>(200, "success", null);
    }

    // 自定义成功响应
    public static <T> Result<T> success(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }

    // 通用错误响应
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error() {
        return new Result<>(500, "error", null);
    }

    // 自定义错误响应
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // 带数据的错误响应（较少使用）
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
    // 分页数据成功响应
    public static <T> Result<PageData<T>> pageSuccess (long total, List<T> rows) {
        return new Result<>(200, "success", new PageData<>(total, rows));
    }
    // 分页数据类
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageData<T> {
        private long total;
        private List<T> rows;
    }
}