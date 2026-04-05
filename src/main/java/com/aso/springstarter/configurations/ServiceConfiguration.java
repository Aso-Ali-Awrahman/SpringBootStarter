package com.aso.springstarter.configurations;

import com.aso.springstarter.repositories.CustomerRepository;
import com.aso.springstarter.repositories.EmployeeRepository;
import com.aso.springstarter.repositories.OrderItemRepository;
import com.aso.springstarter.repositories.OrderRepository;
import com.aso.springstarter.repositories.ProductRepository;
import com.aso.springstarter.security.JwtAuthenticationFilter;
import com.aso.springstarter.security.JwtConfig;
import com.aso.springstarter.security.JwtService;
import com.aso.springstarter.security.JwtServiceImpl;
import com.aso.springstarter.security.UserDetailsServiceImpl;
import com.aso.springstarter.services.CustomerService;
import com.aso.springstarter.services.CustomerServiceImpl;
import com.aso.springstarter.services.EmployeeService;
import com.aso.springstarter.services.EmployeeServiceImpl;
import com.aso.springstarter.services.OrderItemService;
import com.aso.springstarter.services.OrderItemServiceImpl;
import com.aso.springstarter.services.OrderService;
import com.aso.springstarter.services.OrderServiceImpl;
import com.aso.springstarter.services.ProductService;
import com.aso.springstarter.services.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfiguration {

    @Bean
    ProductService productService(ProductRepository productRepository){
        return new ProductServiceImpl(productRepository);
    }

    @Bean
    EmployeeService employeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder){
        return new EmployeeServiceImpl(employeeRepository, passwordEncoder);
    }

    @Bean
    CustomerService customerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return new CustomerServiceImpl(customerRepository, passwordEncoder);
    }

    @Bean
    UserDetailsService userDetailsService(EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        return new UserDetailsServiceImpl(employeeRepository, customerRepository);
    }

    @Bean
    JwtService jwtService(JwtConfig jwtConfig) {
        return new JwtServiceImpl(jwtConfig);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    OrderService orderService(OrderRepository orderRepository) {
        return new OrderServiceImpl(orderRepository);
    }

    @Bean
    OrderItemService orderItemService(OrderItemRepository orderItemRepository) {
        return new OrderItemServiceImpl(orderItemRepository);
    }

}
