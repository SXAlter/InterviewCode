package com.example.helloworld.filter;

import com.example.helloworld.Util.EhcacheUtil;
import com.example.helloworld.Util.JwtUtil;
import com.example.helloworld.domain.LoginUser;
import com.example.helloworld.domain.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

//@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        log.info("JwtAuthenticationTokenFilter执行！{}",token);
        if (!StringUtils.hasText(token)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new LoginUser(new User(),new ArrayList<String>()),null,null));
            filterChain.doFilter(request,response);
            return;
        }
        String userID;

        try {
            Claims claims = JwtUtil.parseJWT(token);
            userID = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }


        log.info("userID:{}",userID);
        LoginUser loginUser = EhcacheUtil.getFromCache("LoggedIn:" + userID, LoginUser.class);
        if(Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        // 设置登录态
        request.setAttribute("userId", userID);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }
}
