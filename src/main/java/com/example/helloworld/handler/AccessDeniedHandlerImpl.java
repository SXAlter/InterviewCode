package com.example.helloworld.handler;

import com.alibaba.fastjson.JSON;
import com.example.helloworld.Util.ResponseResult;
import com.example.helloworld.Util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description: 自定义授权失败异常处理类
 */
@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResponseResult<Object> result = new ResponseResult<>(HttpStatus.FORBIDDEN.value(),"用户授权失败，请联系管理员！");

        log.info("AccessDeniedHandler执行！");
        String jsonString = JSON.toJSONString(result);
        //处理异常
        WebUtils.renderString(response,jsonString);
    }
}
