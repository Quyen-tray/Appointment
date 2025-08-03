package com.hospital.appointmentservice.config;

import com.hospital.appointmentservice.auth.security.ActivityLoggingFilter;
import com.hospital.appointmentservice.auth.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Autowired private JwtFilter jwtFilter;
    @Autowired private ActivityLoggingFilter   activityLoggingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //config AuthenticationManager
        // config security decentralization
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/api/patient/contact").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/auth/logout","/api/user").authenticated()
                        .requestMatchers("/api/patient/**").authenticated()
                        .requestMatchers("/api/auth/login","/api/blogs", "/api/visits", "/api/labrequests", "/api/payment" ).permitAll()
                        .anyRequest().permitAll()
                ).logout(AbstractHttpConfigurer::disable

                ).rememberMe(customizer->customizer
                        .key("remember-me-key")
                        .tokenValiditySeconds(1209600)
                ).addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(activityLoggingFilter,jwtFilter.getClass());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }




}

