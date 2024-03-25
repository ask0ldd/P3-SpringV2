package com.example.immo.configuration;

import com.example.immo.utils.KeyGeneratorUtility;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfiguration {

    // https://github.com/spring-projects/spring-security-samples/blob/6.2.x/servlet/spring-boot/java/oauth2/resource-server/static/src/main/java/example/OAuth2ResourceServerSecurityConfiguration.java
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    // https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-key-sets
    public JwtConfiguration(){
        KeyPair rsaKeys = KeyGeneratorUtility.generateRSAKeys();
        this.publicKey = (RSAPublicKey) rsaKeys.getPublic();
        this.privateKey = (RSAPrivateKey) rsaKeys.getPrivate();
    }

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
}
