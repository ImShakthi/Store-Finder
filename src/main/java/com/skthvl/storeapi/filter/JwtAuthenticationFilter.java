package com.skthvl.storeapi.filter;

import com.skthvl.storeapi.provider.JwtTokenProvider;
import com.skthvl.storeapi.service.InvalidatedTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A filter that intercepts HTTP requests to authenticate users based on JWT (JSON Web Token). This
 * filter extracts the JWT from the "Authorization" header, validates the token, and sets the user's
 * authentication details in the security context.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final InvalidatedTokenService invalidatedTokenService;

  public JwtAuthenticationFilter(
      final JwtTokenProvider jwtTokenProvider,
      final InvalidatedTokenService invalidatedTokenService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.invalidatedTokenService = invalidatedTokenService;
  }

  /**
   * Handles incoming HTTP requests by checking the "Authorization" header for a JWT token. If a
   * valid token is found and verified, the user's authentication details are set into the
   * SecurityContext. Invalid or missing tokens result in an unauthorized response.
   *
   * @param request the current HTTP request to be filtered
   * @param response the current HTTP response for the request
   * @param filterChain the filter chain to pass the request and response to the next filter
   * @throws ServletException if an error occurs during request processing
   * @throws IOException if an input or output exception occurs
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      final String token = header.substring(7);
      log.info("JWT token: {}", token);
      if (invalidatedTokenService.isTokenInvalidated(token)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalidated");
      }
      log.info("Token is invalidated");

      if (jwtTokenProvider.validateToken(token)) {
        final Claims claims = jwtTokenProvider.extractClaims(token);
        log.info("Claims are: {}", claims);
        final String username = claims.getSubject();

        final UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(username, null, null);

        SecurityContextHolder.getContext().setAuthentication(authToken);
      } else {
        log.info("Token is expired");
      }
    }

    filterChain.doFilter(request, response);
  }
}
