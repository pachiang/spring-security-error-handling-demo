package com.pa.demo.configuration;

import com.pa.demo.entity.Customer;
import com.pa.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
@Slf4j
public class CustomUsernamePwdAuthenticationProvider implements AuthenticationProvider {
    private CustomerRepository customerRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Customer customer = customerRepository.findOneByEmail(username);
        if (customer != null) {
            if (Objects.equals(pwd, customer.getPassword())) {
                log.warn(customer.getRole());
                log.warn(getGrantedAuthorities(customer.getRole()).toString());
                return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(customer.getRole()));
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
