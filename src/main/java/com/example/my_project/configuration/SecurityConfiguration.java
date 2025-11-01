package com.example.my_project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.my_project.exeptionHandler.CustomAccessDeniedHandler;
import com.example.my_project.exeptionHandler.CustomAuthenticationEntryPoint;
import com.example.my_project.service.JwtTokenFilterService;

@Configuration
public class SecurityConfiguration {

        private final JwtTokenFilterService jwtTokenFilterService;

        private final CustomAuthenticationEntryPoint authenticationEntryPoint;

        private final CustomAccessDeniedHandler accessDeniedHandler;

        public SecurityConfiguration(
                        JwtTokenFilterService jwtTokenFilterService,
                        CustomAuthenticationEntryPoint authenticationEntryPoint,
                        CustomAccessDeniedHandler accessDeniedHandler) {
                this.jwtTokenFilterService = jwtTokenFilterService;
                this.authenticationEntryPoint = authenticationEntryPoint;
                this.accessDeniedHandler = accessDeniedHandler;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .cors(AbstractHttpConfigurer::disable)
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))
                                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                                .requestMatchers(
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/v2/api-docs/**",
                                                                "/swagger-resources/**",
                                                                "/swagger-ui.html",
                                                                "/webjars/**",
                                                                "/configuration/ui",
                                                                "/configuration/security",
                                                                "/openapi.yaml")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/users/auth").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/users/{userId}")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .requestMatchers(HttpMethod.POST, "/locations").hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/locations/{locationId}")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/locations/{locationId}")
                                                .hasAuthority("ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/locations")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .anyRequest().authenticated())
                                .addFilterBefore(
                                                jwtTokenFilterService, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
