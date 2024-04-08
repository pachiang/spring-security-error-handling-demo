package com.pa.demo.service;

import com.pa.demo.entity.Customer;
import com.pa.demo.exception.CustomerNotFoundException;
import com.pa.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    public com.pa.demo.entity.Customer findOneByEmail(String email) {
        Customer customer = customerRepository.findOneByEmail(email);
        if (customer == null) {
            throw new CustomerNotFoundException("Can't find the customer named: " + email);
        }
        return customer;
    }
}
