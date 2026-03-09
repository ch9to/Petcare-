package com.petcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registro", "/registro/salvar", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Nossa página de login customizada
                        .usernameParameter("email") // <-- IMPORTANTE: Diz ao Spring que o 'username' é o campo 'email'
                        .loginProcessingUrl("/login") // <-- O Spring vai cuidar do POST para /login
                        .defaultSuccessUrl("/home", true) // <-- IMPORTANTE: Para onde ir após o login
                        .failureUrl("/login?error=true") // <-- Para onde ir se o login falhar
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Para onde ir após desconectar
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
