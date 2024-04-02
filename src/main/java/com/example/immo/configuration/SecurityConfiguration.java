package com.example.immo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfiguration(JwtDecoder jwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtDecoder = jwtDecoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // csrf disabled cause stateless
        return http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                // .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .authorizeHttpRequests(authorize -> {
                    authorize
                            // all routes but login, register & swaggers needs to be authenticated
                            .requestMatchers(new AntPathRequestMatcher("/api/auth/login")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/auth/register")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/api/auth/me")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/rentals/**")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/user/**")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/messages")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/auth/*")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/img/**")).hasAnyRole("USER", "ADMIN")
                            .requestMatchers(SWAGGER_WHITELIST).permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .build();
    }

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/**",
            "/configuration/**",
            "/api/v1/auth/**",
    };

    // this will encode the user's
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets
// https://www.baeldung.com/spring-security-map-authorities-jwt
// https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html
// https://www.baeldung.com/spring-security-oauth-resource-server