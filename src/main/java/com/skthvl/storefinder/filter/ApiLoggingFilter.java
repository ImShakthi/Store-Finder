package com.skthvl.storefinder.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * A filter for logging HTTP requests and responses in a Spring Boot application. This filter
 * ensures that each request and response is logged only once per request lifecycle. Specific URIs
 * can be skipped from logging.
 */
@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

  private static final List<String> SKIP_LOGGING_FOR_URI =
      List.of("/api-docs", "/swagger-ui", "/h2-console");

  /**
   * Processes incoming HTTP requests and responses, and logs their details such as method, URI, and
   * optionally their content. This method leverages {@link ContentCachingRequestWrapper} and {@link
   * ContentCachingResponseWrapper} to allow repeated reads of the request and response content
   * streams.
   *
   * @param req the current HTTP request
   * @param res the current HTTP response
   * @param chain the filter chain to pass the request and response to the next filter
   * @throws ServletException if any servlet-specific exception occurs
   * @throws IOException if an input or output exception occurs
   */
  @Override
  protected void doFilterInternal(
      final HttpServletRequest req, final HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    final ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(req);
    final ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(res);

    try {
      chain.doFilter(request, response);

      if (!skipLogging(req)) {
        log.info("Request: {} {}", request.getMethod(), request.getRequestURI());
        Optional.of(new String(request.getContentAsByteArray(), request.getCharacterEncoding()))
            .filter(s -> !s.isEmpty())
            .ifPresent(log::info);

        Optional.of(new String(response.getContentAsByteArray(), response.getCharacterEncoding()))
            .filter(s -> !s.isEmpty())
            .ifPresent(log::info);
      }
    } finally {
      response.copyBodyToResponse();
    }
  }

  private boolean skipLogging(final HttpServletRequest req) {
    return SKIP_LOGGING_FOR_URI.parallelStream().anyMatch(uri -> req.getRequestURI().contains(uri));
  }
}
