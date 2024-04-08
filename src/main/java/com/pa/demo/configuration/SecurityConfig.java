package com.pa.demo.configuration;

import com.pa.demo.filter.JWTTokenValidatorFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;
    @Autowired
    JWTTokenValidatorFilter jwtTokenValidatorFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
//                .addFilterAt(new AuthoritiesLoggingAtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/customers").hasRole("reader")
                        .requestMatchers("/login", "/error").permitAll()
                        .requestMatchers("/**").permitAll() // Allow access to all other URLs
                        .anyRequest().authenticated())
                .addFilterBefore(jwtTokenValidatorFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    exceptionResolver.resolveException(request, response, null, accessDeniedException);
                }))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    exceptionResolver.resolveException(request, response, null, authException);
                }));
        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
