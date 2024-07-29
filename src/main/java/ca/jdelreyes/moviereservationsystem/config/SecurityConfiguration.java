package ca.jdelreyes.moviereservationsystem.config;

import ca.jdelreyes.moviereservationsystem.config.filter.JwtAuthenticationFilter;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers("/api/auth/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/movies/**", "/api/theaters/**", "/api/movie-schedules/**")
                                        .permitAll()
                                        .requestMatchers("/api/seat-reservations/**", "/api/stripe/**")
                                        .hasAuthority(Role.USER.name())
                                        .requestMatchers("/api/movies/**", "/api/theaters/**", "/api/movie-schedules/**")
                                        .hasAuthority(Role.ADMIN.name())
                                        .requestMatchers("/api/actuator/**")
                                        .hasAnyAuthority(Role.ACTUATOR.name(), Role.ADMIN.name())
                                        .anyRequest()
                                        .authenticated()
//                                        .anyRequest()
//                                        .permitAll()
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
