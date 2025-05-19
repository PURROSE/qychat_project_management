package com.purplerosechen.qpm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 后台管理接口
 * @date 15 5月 2025 14:18
 */

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    /** 
     * @description: TODO 测试响应，没啥作用 
     * @author chen
     * @date: 15 5月 2025 14:19
     */ 
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
