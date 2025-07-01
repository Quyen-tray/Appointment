package com.hospital.appointmentservice.config;

import com.hospital.appointmentservice.auth.security.CustomAuthenticationFilter;
import com.hospital.appointmentservice.auth.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Autowired private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //config AuthenticationManager
        // config security decentralization
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/patient").authenticated()
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().permitAll()
                ).logout(customizer->customizer
                        .logoutUrl("/api/auth/logout")
                        .deleteCookies("JSESSIONID")
                ).rememberMe(customizer->customizer
                        .key("remember-me-key")
                        .tokenValiditySeconds(1209600)
                ).addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }




}
