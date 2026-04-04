package com.aso.springstarter.security;

import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.repositories.CustomerRepository;
import com.aso.springstarter.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = employeeRepository.findByEmail(email)
            .map(employee -> (User) employee)
            .orElseGet(() -> customerRepository.findByEmail(email)
                .map(customer -> (User) customer)
                .orElseThrow(() -> new BadCredentialsException("User not found"))
            );

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new BadCredentialsException("User is blocked");
        }

        return new UserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            user.getStatus()
        );
    }

}
