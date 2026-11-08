package chnu.edu.websecurity26.config;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  SecurityConfig
  @version  1.0.0
  @since 08/11/2026 - 16.00
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // REST API is used by non-browser clients (HTTP Client, Postman),
                // so CSRF protection is disabled
                .csrf(AbstractHttpConfigurer::disable)
                // every request needs an authenticated user
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().authenticated())
                // authentication with the Authorization: Basic header
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
