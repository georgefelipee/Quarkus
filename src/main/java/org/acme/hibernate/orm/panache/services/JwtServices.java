package org.acme.hibernate.orm.panache.services;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.auth.principal.JWTAuthContextInfo;

import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.smallrye.jwt.build.spi.JwtProvider;
import io.smallrye.jwt.util.KeyUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.enterprise.context.ApplicationScoped;
import netscape.javascript.JSObject;
import org.acme.hibernate.orm.panache.User;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtServices {

    private JWTAuthContextInfo contextInfo;

    public JwtServices() throws GeneralSecurityException, IOException {
        RSAPublicKey publicKey = (RSAPublicKey) KeyUtils.readPublicKey("/publicKey.pem");
         contextInfo = new JWTAuthContextInfo(publicKey, "https://example.com/issuer");
    }

    public String generateToken(String  username) {
        Set<String> groups = new HashSet<>(
                Arrays.asList("user", "admin")
        );
        long duration =  System.currentTimeMillis() + 360;

        return Jwt.issuer("https://example.com/issuer")
                .subject(username)
                .groups(groups)
                .claim(Claims.groups.name(), Arrays.asList("user"))
                .claim(Claims.acr.birthdate.name(), "2001-07-13")
                .expiresAt(duration)
                .sign();

    }

    public User decodeToken(String token) throws InvalidJwtException {
        if(token !=null && token.startsWith("Bearer ")){
            token = token.substring(7);
        }

        var test = JWT.parse(token);
        JsonObject value = (JsonObject) test.getValue("payload");
        var username = value.getValue("sub");

        return User.find("username", username).firstResult();
    }
}
