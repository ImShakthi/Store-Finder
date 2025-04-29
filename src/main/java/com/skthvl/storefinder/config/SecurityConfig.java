package com.skthvl.storefinder.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for the application, responsible for setting up authentication,
 * authorization, CORS, CSRF, and security filter chains.
 */
@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

  @Value("#{'${store-api.security.cors-allowed-origins}'.split(',')}")
  private List<String> corsAllowedUrls;

  private static final String[] PUBLIC_NON_APP_APIs =
      new String[] {
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

  private static final String[] PUBLIC_NO_AUTH_APP_APIs =
      new String[] {"/api/v1/stores/nearby", "/api/v1/stores", "/api/v1/stores/{storeId}"};

  private static final String[] AUTH_APP_APIs = new String[] {};

  //  private final JwtAuthenticationFilter jwtFilter;
  //
  //  public SecurityConfig(final JwtAuthenticationFilter jwtFilter) {
  //    this.jwtFilter = jwtFilter;
  //  }

  /**
   * Configures the SecurityFilterChain for the application, defining security settings such as
   * disabling CSRF, configuring CORS, managing authentication policies for different API endpoints,
   * and adding a JWT filter for stateless session management.
   *
   * @param http the {@link HttpSecurity} object used to configure security settings
   * @return a configured {@link SecurityFilterChain} instance
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth ->
                auth
                    // public apis (without JWT)
                    //                    .requestMatchers(PUBLIC_NON_APP_APIs)
                    //                    .permitAll()
                    //
                    //                    .requestMatchers(PUBLIC_NO_AUTH_APP_APIs)
                    //                    .permitAll()

                    // auth apis (with JWT)
                    //                    .requestMatchers(AUTH_APP_APIs)
                    //                    .authenticated()

                    // Other APIs
                    .anyRequest()
                    .permitAll())

        // Stateless session (required for JWT)
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

        // added JWT filter
        //        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

        // Exception handling
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                        (request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_NOT_FOUND)));

    return http.build();
  }

  /**
   * Provides a Spring Bean that configures and retrieves an {@link AuthenticationManager} instance
   * from the provided {@link AuthenticationConfiguration}.
   *
   * @param config the {@link AuthenticationConfiguration} used to obtain the {@link
   *     AuthenticationManager}
   * @return the configured {@link AuthenticationManager} instance
   * @throws Exception if an error occurs while retrieving the {@link AuthenticationManager}
   */
  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Configures and provides a CORS (Cross-Origin Resource Sharing) configuration source to handle
   * requests coming from different origins.
   *
   * <p>The method defines allowed origins, methods, headers, and credentials settings for CORS. It
   * registers these configurations with a {@link UrlBasedCorsConfigurationSource} for
   * application-wide use.
   *
   * @return a configured {@link CorsConfigurationSource} instance that specifies the CORS settings.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(corsAllowedUrls);
    //    config.setAllowedOrigins(List.of("http://localhost:3000/"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true); // required if using cookies or Authorization headers

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }

  /**
   * Provides a Spring Bean that configures and returns a {@link PasswordEncoder} for securing user
   * passwords.
   *
   * <p>This method returns an instance of {@link BCryptPasswordEncoder}, which applies the BCrypt
   * hashing algorithm to encode passwords. BCrypt is a secure and adaptive algorithm well-suited
   * for password storage.
   *
   * @return a configured {@link PasswordEncoder} instance using BCrypt hashing.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
