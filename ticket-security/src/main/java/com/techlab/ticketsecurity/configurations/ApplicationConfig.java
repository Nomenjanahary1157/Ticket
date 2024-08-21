package com.techlab.ticketsecurity.configurations;

import com.techlab.ticketservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class contains the configuration for the application.
 * It uses Spring's annotations to automatically scan and configure the necessary beans.
 *
 * @author Your Name
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.techlab.ticketservice.services")
public class ApplicationConfig {

    /**
     * This field is an instance of the UserService class, which is used to interact with the user data.
     * It is autowired by Spring to automatically inject the dependency.
     */
    @Autowired
    private UserService userService;

    /**
     * This method is annotated with @Bean, which tells Spring to create an instance of the UserDetailsService class.
     * It takes a String parameter representing the username and returns the UserDetails object from the userService.
     * If the user is not found, it throws a UsernameNotFoundException.
     *
     * @param username the username of the user to find
     * @return the UserDetails object of the user, or throws UsernameNotFoundException if not found
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userService.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * This method is annotated with @Bean, which tells Spring to create an instance of the DaoAuthenticationProvider class.
     * It sets the UserDetailsService and PasswordEncoder beans, and returns the configured authentication provider.
     *
     * @return the configured DaoAuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * This method is annotated with @Bean, which tells Spring to create an instance of the AuthenticationManager class.
     * It takes an AuthenticationConfiguration instance as a parameter and returns the configured authentication manager.
     *
     * @param config the AuthenticationConfiguration instance
     * @return the configured AuthenticationManager instance
     * @throws Exception if any error occurs during the configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * This method is annotated with @Bean, which tells Spring to create an instance of the BCryptPasswordEncoder class.
     * It returns a new instance of the BCryptPasswordEncoder.
     *
     * @return a new instance of the BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
