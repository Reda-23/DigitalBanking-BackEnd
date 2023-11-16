package org.ebankbackend.controller;

import lombok.RequiredArgsConstructor;
import org.ebankbackend.security.model.AppUser;
import org.ebankbackend.service.UserSecurityService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
@CrossOrigin("*")
public class SecurityRESTController {

    private final UserSecurityService userSecurityService;
    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;


    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public List<AppUser> users(){
        return userSecurityService.appUsers();
    }

    @PostMapping("/auth")
    public Map<String,String> jwtToken(String username , String password){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username,password));
        Instant instant = Instant.now();
        String scope = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(15, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope",scope)
                .build();
JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
        .from(JwsHeader.with(MacAlgorithm.HS512)
                .build(),jwtClaimsSet);
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);
        return Map.of("ACCESS_TOKEN",jwt.getTokenValue());

    }

}
