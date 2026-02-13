package com.aso.springstarter.configurations;

import com.aso.springstarter.repositories.CustomerRepository;
import com.aso.springstarter.repositories.EmployeeRepository;
import com.aso.springstarter.repositories.ProductRepository;
import com.aso.springstarter.services.CustomerService;
import com.aso.springstarter.services.CustomerServiceImpl;
import com.aso.springstarter.services.EmployeeService;
import com.aso.springstarter.services.EmployeeServiceImpl;
import com.aso.springstarter.services.ProductService;
import com.aso.springstarter.services.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    ProductService productService(ProductRepository productRepository){
        return new ProductServiceImpl(productRepository);
    }

    @Bean
    EmployeeService employeeService(EmployeeRepository employeeRepository){
        return new EmployeeServiceImpl(employeeRepository);
    }

    @Bean
    CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerServiceImpl(customerRepository);
    }

}
