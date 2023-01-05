package com.vweinert.fedditbackend.security.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import com.vweinert.fedditbackend.repository.UserRepository;
import com.vweinert.fedditbackend.security.services.UserDetailsImpl;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private Map<String, Object> rsaKeys;
    private JwtParser parser;
    private final UserRepository userRepository;
    @Value("${feddit.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    public JwtUtils(UserRepository userRepository) throws Exception{
        this.userRepository = userRepository;
        this.rsaKeys =  getRsaKeys();
        PublicKey publicKey = (PublicKey) rsaKeys.get("public");
        this.parser = Jwts.parserBuilder().setSigningKey(publicKey).build();
        logger.debug("jwtutils initialized");
    }
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        PrivateKey privateKey = (PrivateKey) rsaKeys.get("private");
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(privateKey,SignatureAlgorithm.RS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {

        return parser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long getUserIdFromJwtToken(String token) {

        return userRepository.findByUsername(
                parser.parseClaimsJws(token)
                        .getBody()
                        .getSubject())
                .orElseThrow(RuntimeException::new).getId();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            parser.parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.error("JWT claims from an old feddit instance: {}", e.getMessage());
        }

        return false;
    }

    private Map<String,Object> getRsaKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("private", privateKey);
        keys.put("public",publicKey);
        return keys;
    }
}
