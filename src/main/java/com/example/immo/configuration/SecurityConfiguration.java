package com.example.immo.configuration;

import com.example.immo.utils.KeyGeneratorUtility;
import lombok.Value;
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
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // https://github.com/spring-projects/spring-security-samples/blob/6.2.x/servlet/spring-boot/java/oauth2/resource-server/static/src/main/java/example/OAuth2ResourceServerSecurityConfiguration.java

    /*
        @Value("${file.path}")
        private String filePath;
     */

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    // https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets
    public SecurityConfiguration(KeyGeneratorUtility keyGeneratorUtility){
        KeyPair rsaKeys = keyGeneratorUtility.generateRSAKeys();
        this.publicKey = (RSAPublicKey) rsaKeys.getPublic();
        this.privateKey = (RSAPrivateKey) rsaKeys.getPrivate();
    }

    // https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html#oauth2-resource-server-custom-jwt
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /*@Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // @formatter:off
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        // @formatter:on
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }*/

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

    // !!!!!!!!!!!!!! protect routes
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                            .requestMatchers(new AntPathRequestMatcher("/img/**")).permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer(
                        // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
    }
}