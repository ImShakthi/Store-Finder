package com.skthvl.storeapi.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.skthvl.storeapi.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for the application, responsible for setting up authentication,
 * authorization, CORS, CSRF, and security filter chains.
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("#{'${store-api.security.cors-allowed-origins}'.split(',')}")
  private List<String> corsAllowedUrls;

  private static final String[] PUBLIC_NON_APP_APIs = {
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/api-docs/**",
    "/h2-console/**",
    "/webjars/**",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security"
  };

  private static final String[] PUBLIC_NON_AUTH_APP_APIs = {
    "/api/v1/cities",
    "/api/v1/store-location-types",
    "/api/v1/auth/login",
    "/api/v1/users"
  };

  private final JwtAuthenticationFilter jwtFilter;

  public SecurityConfig(final JwtAuthenticationFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(PUBLIC_NON_APP_APIs)
                    .permitAll()
                    .requestMatchers(PUBLIC_NON_AUTH_APP_APIs)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/stores/nearby/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/stores")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/stores/{storeId}/operation-status")
                    .permitAll()

                    // Authenticated routes
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/stores")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/v1/stores")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/stores")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/users")
                    .authenticated()

                    // Everything else is denied
                    .anyRequest()
                    .authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                        (req, res, ex1) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .accessDeniedHandler(
                        (req, res, ex2) -> res.sendError(HttpServletResponse.SC_FORBIDDEN)))
    ;

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(corsAllowedUrls);
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
