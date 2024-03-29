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
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtDecoder jwtDecoder;

    // https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets
    public SecurityConfiguration(JwtDecoder jwtDecoder){
        this.jwtDecoder = jwtDecoder;
    }

    // !!! create passwordconfiguration class
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                // https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
                .cors(Customizer.withDefaults())
                // .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .authorizeHttpRequests(authorize -> {
                    authorize
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
                        // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .build();
    }

    // https://www.baeldung.com/spring-security-map-authorities-jwt
    // Adding a prefix to the roles contained into the JWT.
    // Ex : JWT Role : "USER" converted to "ROLE_USER".
    // !!!!!!!! move to JwtConfiguration
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
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
}