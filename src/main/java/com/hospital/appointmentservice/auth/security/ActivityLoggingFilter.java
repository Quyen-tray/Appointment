package com.hospital.appointmentservice.auth.security;

import com.hospital.appointmentservice.auth.service.UserActivityLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ActivityLoggingFilter extends OncePerRequestFilter {
    @Autowired private UserActivityLogService userActivityLogService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
//  bo qua login and register
        if (request.getRequestURI().startsWith("/login") || request.getRequestURI().startsWith("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String userName = getCurrentUserName();
        filterChain.doFilter(request, response);
        long duration = System.currentTimeMillis()-start;
        userActivityLogService.log(userName,method,uri,duration,request.getRemoteAddr());

    }

    private String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            return authentication.getName();
        }else
            return "ANONYMOUS";
    }
}
