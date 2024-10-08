package Growup.spring.config.jwt;

import Growup.spring.config.jwt.filter.JwtAuthenticationFilter;
import Growup.spring.config.jwt.handler.CustomAccessDeniedHandler;
import Growup.spring.config.jwt.handler.CustomAuthenticationEntryPoint;
import Growup.spring.config.jwt.util.JwtUtil;
import Growup.spring.config.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)  // 인증을 UI로 할 것이 아니라서 disable을 한 것
                .csrf(AbstractHttpConfigurer::disable) // CSRF도 같음
                .cors(AbstractHttpConfigurer::disable) // CORS 삭젯
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/login/kakao").permitAll()
                        .requestMatchers("/api/v1/auth/kakao").permitAll()

                        .requestMatchers("/api/v1/auth/**").hasAnyRole("USER")

                        .requestMatchers("**").permitAll()
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JWT Filter 를 필터체인에 끼워넣어줌
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil,redisUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .build();

    }
}
