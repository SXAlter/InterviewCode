package com.example.helloworld.service;

import com.example.helloworld.Util.ResponseResult;
import com.example.helloworld.domain.User;

public interface LoginService {

    ResponseResult<Object> login(User user);

}
