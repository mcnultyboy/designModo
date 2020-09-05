package com.yb.controller;

import com.yb.utils.Metrics;
import com.yb.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private Metrics metrics;

    public UserController() {
    }

    @RequestMapping(value = "/register")
    public String register(){
        long startTime = System.currentTimeMillis();
        metrics.recordTimestamp("register", new Double(startTime));
        // 模拟调用时间
        TimeUtil.sleepInRandomSeconds();
        long respTime = System.currentTimeMillis() - startTime;
        metrics.recordResponseTime("register", new Double(respTime));
        return "register ok";
    }

    @RequestMapping(value = "/login")
    public String login(){
        long startTime = System.currentTimeMillis();
        metrics.recordTimestamp("login", new Double(startTime));
        // 模拟调用时间
        TimeUtil.sleepInRandomSeconds();
        long respTime = System.currentTimeMillis() - startTime;
        metrics.recordResponseTime("login", new Double(respTime));
        return "login ok";
    }
}
