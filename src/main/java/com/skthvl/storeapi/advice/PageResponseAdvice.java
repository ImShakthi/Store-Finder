package com.skthvl.storeapi.advice;

import com.skthvl.storeapi.model.response.PaginatedResponse;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * REST Controller advice that intercepts Spring Data Page responses and transforms them into
 * PaginatedResponse format for consistent API responses.
 */
@RestControllerAdvice
public class PageResponseAdvice implements ResponseBodyAdvice<Object> {

  /**
   * Determines if this advice should be applied to the response.
   *
   * @param returnType the return type of the controller method
   * @param converterType the selected converter type
   * @return true if the response is a Page object
   */
  @Override
  public boolean supports(
      final MethodParameter returnType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    // Only intercept responses that are Page<T>
    return Page.class.isAssignableFrom(returnType.getParameterType());
  }

  /**
   * Transforms Page responses into PaginatedResponse format before writing to the response body.
   *
   * @param body the body to be written
   * @param returnType the return type of the controller method
   * @param selectedContentType the content type selected through content negotiation
   * @param selectedConverterType the converter type selected to write to the response
   * @param request the current request
   * @param response the current response
   * @return the body that was passed in or a new PaginatedResponse instance
   */
  @Override
  public Object beforeBodyWrite(
      final Object body,
      final MethodParameter returnType,
      final MediaType selectedContentType,
      final Class<? extends HttpMessageConverter<?>> selectedConverterType,
      final org.springframework.http.server.ServerHttpRequest request,
      final org.springframework.http.server.ServerHttpResponse response) {

    if (body instanceof Page<?> page) {
      return new PaginatedResponse<>(page);
    }
    return body;
  }
}
