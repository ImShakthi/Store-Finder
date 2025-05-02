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

  /**
   * Configures the security filter chain for the application using the provided HttpSecurity
   * instance.
   *
   * <p>This method defines a security configuration that specifies: - CSRF protection is disabled.
   * - CORS configuration is determined by the provided CorsConfigurationSource. - Specific
   * endpoints are either publicly accessible or require authentication. - Stateless session
   * management is enforced. - JWT-based security is handled by adding a filter before the
   * UsernamePasswordAuthenticationFilter. - Exception handling is implemented for unauthorized
   * access and forbidden resource access.
   *
   * @param http the HttpSecurity instance to configure security settings, such as protection
   *     mechanisms, session management, and filter chains
   * @return a configured SecurityFilterChain instance for managing the security of the application
   * @throws Exception if an error occurs during the configuration process
   */
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
                    .requestMatchers(HttpMethod.GET, "/api/v1/stores/{storeId}")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/stores/{storeId}/operation-status")
                    .permitAll()

                    // Authenticated routes
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/logout")
                    .authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/v1/stores")
                    .authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/v1/stores/{storeId}")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/stores/{storeId}")
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
                        (req, res, ex2) -> res.sendError(HttpServletResponse.SC_FORBIDDEN)));

    return http.build();
  }

  /**
   * Provides an AuthenticationManager bean for managing authentication within the application.
   *
   * <p>This method retrieves the AuthenticationManager instance from the provided
   * AuthenticationConfiguration to facilitate the authentication mechanisms in the system.
   *
   * @param config the AuthenticationConfiguration instance used to configure and obtain the
   *     AuthenticationManager
   * @return an AuthenticationManager instance for handling authentication processes
   * @throws Exception if an error occurs while retrieving the AuthenticationManager
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Creates and provides a PasswordEncoder bean for encoding passwords securely.
   *
   * <p>This method returns a PasswordEncoder instance that uses the BCrypt hashing algorithm to
   * encode passwords, ensuring that password storage is secure and resistant to attacks such as
   * brute force and hash collision.
   *
   * @return a PasswordEncoder instance for securely encoding passwords
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures and provides a CorsConfigurationSource bean to manage Cross-Origin Resource Sharing
   * (CORS) settings.
   *
   * <p>This method sets up a CORS configuration that: - Allows specified origins configured in the
   * `corsAllowedUrls` field. - Permits HTTP methods including GET, POST, PUT, DELETE, and OPTIONS.
   * - Allows all headers. - Supports credentials (cookies, authorization headers, etc.).
   *
   * @return a configured CorsConfigurationSource instance for handling CORS settings.
   */
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
