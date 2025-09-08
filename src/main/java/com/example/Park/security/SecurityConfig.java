package com.example.Park.security;

import com.example.Park.User.user_master_repo;
import com.example.Park.User.User_master;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final user_master_repo userRepo;

    public SecurityConfig(user_master_repo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User_master user = userRepo.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return User.withUsername(user.getEmail())   // principal = email
                    .password(user.getPassword())      // plain text for now
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // swap to BCrypt in prod
    }

    @Bean
    public AuthenticationManager authManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/login1", "/register", "/css/**", "/js/**", "/videos/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login1")
                        .usernameParameter("email")    // use email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/register_visitor", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}