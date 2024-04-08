package com.pa.demo.controller;

import com.pa.demo.dto.APIResponse;
import com.pa.demo.entity.Customer;
import com.pa.demo.repository.CustomerRepository;
import com.pa.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CustomerController {
    CustomerService customerService;
    CustomerRepository customerRepository;
    @GetMapping("/customers/{email}")
    public ResponseEntity<APIResponse> getCustomerByEmail(@PathVariable("email") String email) {
        APIResponse<Customer> apiResponse = APIResponse.
                <Customer>builder()
                .status("SUCCESS")
                .results(customerService.findOneByEmail(email))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/customers")
    public ResponseEntity<APIResponse> getCustomers() {
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(0, 2));
        log.warn(customers.toString());
        APIResponse<Page<Customer>> apiResponse = APIResponse.
                <Page<Customer>>builder()
                .status("SUCCESS")
                .results(customers)
                .build();
        log.warn(apiResponse.toString());
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
