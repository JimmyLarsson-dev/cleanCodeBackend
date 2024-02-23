package com.example.cleancode.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;


import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {


    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtAuthConverterProperties jwtAuthConverterProperties;

    public JwtAuthConverter(JwtAuthConverterProperties jwtAuthConverterProperties) {
        this.jwtAuthConverterProperties = jwtAuthConverterProperties;
    }

    /* Converts a Jwt into an AbstractAuthenticationToken, combining authorities from
     JwtGrantedAuthoritiesConverter and resource roles extracted from the Jwt */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());

        // Creates a JwtAuthenticationToken with Jwt, authorities, and the principal claim name
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    // Retrieves the principal claim name from Jwt based on the configured attribute
    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if(jwtAuthConverterProperties.getPrincipalAttribute() != null) {
            claimName = jwtAuthConverterProperties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    // Extracts resource roles from the Jwt's "resource_access" claim
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if(resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(jwtAuthConverterProperties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null ){
            return  Set.of(); // Returns an empty set if no resource roles are found.
        }

        // Maps resource roles to SimpleGrantedAuthority with "ROLE_" prefix
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}



