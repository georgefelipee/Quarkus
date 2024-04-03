package org.acme.hibernate.orm.panache.services;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtServices {

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
}
