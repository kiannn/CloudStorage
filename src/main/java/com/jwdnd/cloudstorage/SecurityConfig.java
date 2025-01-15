package com.jwdnd.cloudstorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChains(HttpSecurity http, @Value("${base.url}") String base) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/home/**", "/notes/**", "/files/**", "/credentials/**")//,"/js/imgs/**")
                .authenticated()
                .anyRequest()
                .permitAll()
        )
                .formLogin(f -> f.loginPage("/login")
                .defaultSuccessUrl(base + "home", true));

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(a -> a.frameOptions(b -> b.sameOrigin()));
        
        return http.build();

    }

}
