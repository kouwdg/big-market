package org.example.trigger.filter;

import io.jsonwebtoken.Claims;
import org.example.domain.login.model.entity.UserAccountEntity;
import org.example.domain.login.service.ILoginService;
import org.example.types.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: jwt过滤器
 * @date 2025/3/25 11:02
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Lazy
    private ILoginService loginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (token==null){
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        String subject;
        try {
            Claims claims = JWTUtil.parseJWT(token);
            subject = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }

        String userId=subject;
        //从redis中获取用户信息
        UserAccountEntity loginUser = loginService.IsUserAccountInRedis(userId);
        if (loginUser==null){
            //放行
            filterChain.doFilter(request,response);
            return;
        }

        //存入SecurityContextHolder
        //todo 获取权限信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);

    }
}
