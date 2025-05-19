package com.purplerosechen.qpm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 统一错误页面显示处理
 * @date 19 5月 2025 14:34
 */

@Slf4j
@RestControllerAdvice
public class ErrorController{
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e) {
        // 记录日志
        log.error("发生异常:{}",e.getMessage(), e);

        return e.getMessage();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("发生异常:{}",e.getMessage(), e);
        return e.getMessage();
    }
}
