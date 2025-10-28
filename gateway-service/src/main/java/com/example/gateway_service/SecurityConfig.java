package com.example.gateway_service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.types.FilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String SECRET = "changeit"; // must match auth-service.jwt.secret in real deployment

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http.csrf().disable();
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/learning/**").permitAll()
            .requestMatchers("/users/**").hasAnyAuthority("USUARIO","ADMIN")
            .requestMatchers("/learning/**").hasAnyAuthority("USUARIO","PRODUTORDECONTEUDO","ADMIN")
            .requestMatchers("/recruitment/**").hasAnyAuthority("RECRUTADOR","ADMIN")
            .requestMatchers("/subscriptions/**").authenticated()
            .requestMatchers("/reports/**").hasAuthority("ADMIN")
            .anyRequest().authenticated()
        );
        http.addFilterBefore(new JwtAuthorizationFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

    static class JwtAuthorizationFilter extends BasicAuthenticationFilter {
        public JwtAuthorizationFilter() {
            super(authentication -> authentication);
        }

        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }
            String token = header.substring(7);
            try {
                Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
                DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
                String role = jwt.getClaim("role").asString();
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, authorities);
                // set context
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // invalid token - do nothing, treat as anonymous
            }
            chain.doFilter(request, response);
        }
    }
}