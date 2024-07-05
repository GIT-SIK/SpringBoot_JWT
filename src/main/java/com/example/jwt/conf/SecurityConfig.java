package com.example.jwt.conf;

import com.example.jwt.security.JwtAuthFilter;
import com.example.jwt.security.JwtUtil;
import com.example.jwt.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig  {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    //** accessDenied / AuthEntryPoint 핸들러 **//
//    private final CustomAccessDeniedHandler accessDeniedHandler;
//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 미인증 사용자까지 허용할 주소 리스트
     */
    private static final String[] AUTH_WHITELIST = {
            "/logout", "/login"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF, CORS
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        //세션 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

//        http.exceptionHandling((exceptionHandling) -> exceptionHandling
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .accessDeniedHandler(accessDeniedHandler)
//        );

        // 권한 규칙
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll() /* 모든 허용 주소 */
                        .anyRequest().permitAll()
//                       .anyRequest().authenticated()
        );

        return http.build();
    }

}