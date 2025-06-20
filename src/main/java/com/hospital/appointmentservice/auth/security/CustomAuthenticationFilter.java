package com.hospital.appointmentservice.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private static final Logger logger =  LoggerFactory.getLogger(CustomAuthenticationFilter.class);


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request,
                                                                                  HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //Create object authentication include login information of user
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        //authentication authRequest with authenticate()
        Authentication authentication = authenticationManager.authenticate(authRequest);
        if(authentication != null){
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Logging kết quả xác thực
        logger.info("Authentication result: Username: {}, Authorities: {}",
                authentication.getName(),
                authentication.getAuthorities());

        return authentication;
    }
}
