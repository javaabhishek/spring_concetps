package com.eazybank.config;

import com.eazybank.model.Customer;
import com.eazybank.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//@Service
public class EazyBankUserDetailsManager {/*implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public EazyBankUserDetailsManager(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        String userName, password;
        List<GrantedAuthority> authorities=null;
        Customer customer = customerRepository.findByEmail(email);
        if (customer==null) {
            throw new UsernameNotFoundException("User details not found for the user : " + customer);
        } else{
            userName = customer.getEmail();
            password = customer.getPwd();
            authorities= new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(customer.getRole()));
        }
        return new User(userName,password,authorities);
    }*/
}
