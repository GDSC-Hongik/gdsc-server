package com.gdschongik.gdsc.global.config;

import static com.gdschongik.gdsc.global.common.constant.EnvironmentConstant.*;
import static com.gdschongik.gdsc.global.common.constant.UrlConstant.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.security.config.Customizer.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdschongik.gdsc.domain.auth.application.JwtService;
import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.global.annotation.ConditionalOnProfile;
import com.gdschongik.gdsc.global.common.constant.SwaggerUrlConstant;
import com.gdschongik.gdsc.global.property.SwaggerProperty;
import com.gdschongik.gdsc.global.security.CustomSuccessHandler;
import com.gdschongik.gdsc.global.security.CustomUserService;
import com.gdschongik.gdsc.global.security.JwtExceptionFilter;
import com.gdschongik.gdsc.global.security.JwtFilter;
import com.gdschongik.gdsc.global.util.CookieUtil;
import com.gdschongik.gdsc.global.util.EnvironmentUtil;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final ObjectMapper objectMapper;
    private final EnvironmentUtil environmentUtil;
    private final SwaggerProperty swaggerProperty;

    private static void defaultFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    @Bean
    @Order(1)
    @ConditionalOnProfile({DEV, LOCAL})
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        defaultFilterChain(http);

        http.securityMatcher(getSwaggerUrls())
                .oauth2Login(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());

        http.authorizeHttpRequests(
                environmentUtil.isDevProfile()
                        ? authorize -> authorize.anyRequest().authenticated()
                        : authorize -> authorize.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        defaultFilterChain(http);

        http.oauth2Login(
                oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customUserService(memberRepository)))
                        .successHandler(customSuccessHandler(jwtService, cookieUtil))
                        .failureHandler((request, response, exception) -> response.setStatus(401)));

        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint((request, response, authException) -> response.setStatus(401)));

        http.addFilterAfter(jwtExceptionFilter(objectMapper), LogoutFilter.class);
        http.addFilterAfter(jwtFilter(jwtService, cookieUtil), LogoutFilter.class);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/oauth2/**")
                .permitAll()
                .requestMatchers("/gdsc-actuator/**")
                .permitAll()
                .requestMatchers("/onboarding/**")
                .authenticated()
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated());

        return http.build();
    }

    private static String[] getSwaggerUrls() {
        return Arrays.stream(SwaggerUrlConstant.values())
                .map(SwaggerUrlConstant::getValue)
                .toArray(String[]::new);
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.withUsername(swaggerProperty.getUsername())
                .password(passwordEncoder().encode(swaggerProperty.getPassword()))
                .roles("SWAGGER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUserService customUserService(MemberRepository memberRepository) {
        return new CustomUserService(memberRepository);
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler(JwtService jwtService, CookieUtil cookieUtil) {
        return new CustomSuccessHandler(jwtService, cookieUtil);
    }

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService, CookieUtil cookieUtil) {
        return new JwtFilter(jwtService, cookieUtil);
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter(ObjectMapper objectMapper) {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        if (environmentUtil.isProdProfile()) {
            configuration.addAllowedOriginPattern(PROD_CLIENT_URL);
        }

        if (environmentUtil.isDevProfile()) {
            configuration.addAllowedOriginPattern(DEV_CLIENT_URL);
            configuration.addAllowedOriginPattern(LOCAL_REACT_CLIENT_URL);
            configuration.addAllowedOriginPattern(LOCAL_REACT_CLIENT_SECURE_URL);
            configuration.addAllowedOriginPattern(LOCAL_VITE_CLIENT_URL);
            configuration.addAllowedOriginPattern(LOCAL_VITE_CLIENT_SECURE_URL);
        }

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader(SET_COOKIE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
