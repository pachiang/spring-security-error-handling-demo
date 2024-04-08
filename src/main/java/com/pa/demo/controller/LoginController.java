package com.pa.demo.controller;

import com.pa.demo.constant.SecurityConstants;
import com.pa.demo.dto.APIResponse;
import com.pa.demo.dto.AuthRequest;
import com.pa.demo.entity.Customer;
import com.pa.demo.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@RestController
@Slf4j
public class LoginController {
    private CustomerRepository customerRepository;
    private AuthenticationProvider authenticationProvider;
    @GetMapping("/api/v1/user")
    public Customer getUserDetailsAfterLogin(Principal user) {
        return customerRepository.findOneByEmail(user.getName());
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login (@RequestBody AuthRequest authRequest) throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        Authentication authenticated = authenticationProvider.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder().setIssuer("PAC").setSubject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", populateAuthorities(authenticated.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 30000000))
                .signWith(key).compact();


        Cookie cookie = new Cookie(SecurityConstants.JWT_COOKIE_NAME, jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.getName() + "=" + cookie.getValue());

        APIResponse<String> apiResponse = APIResponse.
                <String>builder()
                .status("SUCCESS")
                .results(jwt)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(responseHeaders)
                .body(apiResponse);
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority: collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
