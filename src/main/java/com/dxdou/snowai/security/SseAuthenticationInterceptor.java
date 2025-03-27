package com.dxdou.snowai.security;

import com.dxdou.snowai.config.JwtConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;


/**
 * sse认证拦截器
 *
 * @author foreverdxdou
 */
@Slf4j
@Component
public class SseAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    @Lazy
    private JwtConfig jwtConfig;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 获取请求头中的认证信息
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"认证失败\"}");
            return false;
        }
        String jwt = token.substring(7);
        if (jwtConfig.validateToken(jwt)) {
            String username = jwtConfig.getUsernameFromToken(jwt);
            
            if (username != null && userDetailsService != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return true;
            }
            log.error("token校验失败 username--" + username);
        }
        log.error("token校验失败 jwt--" + jwt);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"code\":401,\"message\":\"认证失败\"}");
        return false;
    }
}