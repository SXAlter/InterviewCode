package com.example.helloworld.controller;

import com.example.helloworld.Util.JsonUtil;
import com.example.helloworld.Util.ResponseResult;
import com.example.helloworld.domain.User;
import com.example.helloworld.service.LoginService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class TestController {

    @Resource
    private LoginService loginService;

    @PostMapping ("/api/user/login")
    public ResponseResult<Object> login(@RequestBody User user) {
        log.info("1");
        ResponseResult<Object> responseResult = null;
        try {
            log.info("user: {}", JsonUtil.toJson(user));
            responseResult = loginService.login(user);
        }catch (Exception e) {
            log.error("登录异常", e);
        }
        return responseResult;
    }

    @GetMapping("/api/helloworld")
    public String helloWorld() {
        return "HelloWorld";
    }

}
