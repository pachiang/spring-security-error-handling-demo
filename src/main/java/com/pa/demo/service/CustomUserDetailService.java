package com.pa.demo.service;

import com.pa.demo.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CustomUserDetailService implements UserDetailsService {
    private CustomerService customerService;
    public CustomUserDetailService(CustomerService customerService) {
        this.customerService = customerService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerService.findOneByEmail(username);
        if (null == customer) {
            throw new UsernameNotFoundException("user not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customer.getRole()));
        UserDetails userDetails = User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .authorities(authorities)
                .build();
        return userDetails;
    }
}
