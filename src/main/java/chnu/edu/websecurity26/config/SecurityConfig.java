package chnu.edu.websecurity26.config;

/*
  @author   Artem Petrakovskyi
  @project   web-security26
  @class  SecurityConfig
  @version  1.1.0
  @since 08/11/2026 - 16.00
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                // access rules are checked from top to bottom, the first match wins
                .authorizeHttpRequests(req -> req
                        // public pages
                        .requestMatchers("/", "/index.html", "/error").permitAll()
                        // greetings for different roles
                        .requestMatchers("/api/v1/books/hello/admin").hasRole("ADMIN")
                        .requestMatchers("/api/v1/books/hello/user").hasRole("USER")
                        .requestMatchers("/api/v1/books/hello/man").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/books/hello/unknown").authenticated()
                        // books: users and admins read, create and update; only admins delete
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/books/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/books/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasRole("ADMIN")
                        // everything else needs any logged-in user
                        .anyRequest().authenticated())
                // authentication with the Authorization: Basic header
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Three users with different roles, kept in memory.
     * {noop} means the password is stored without encoding (only for the lab).
     */
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles("USER")
                .build();

        UserDetails guest = User.builder()
                .username("guest")
                .password("{noop}guest")
                .roles("GUEST")
                .build();

        return new InMemoryUserDetailsManager(admin, user, guest);
    }
}
