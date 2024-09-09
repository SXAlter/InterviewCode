package com.example.helloworld.service.impl;

import com.example.helloworld.Util.EhcacheUtil;
import com.example.helloworld.Util.JwtUtil;
import com.example.helloworld.Util.ResponseResult;
import com.example.helloworld.domain.LoginUser;
import com.example.helloworld.domain.User;
import com.example.helloworld.service.LoginService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult<Object> login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        log.info("authenticationToken:{}",authenticationToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authentication)) {
            throw new RuntimeException("登陆失败！");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        String jwt = JwtUtil.createJWT(userId);
        Map<String, String> map = new HashMap<>();
        map.put("token",jwt);

        // 非数据库案例，简单地使用本地缓存代替redis缓存
        EhcacheUtil.putIntoCache("LoggedIn:" + userId, loginUser);

        return new ResponseResult<>(200, "登陆成功", map);
    }
}
