package com.skthvl.storeapi.model.response;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * A generic record representing paginated response data. Provides a convenient way to serialize
 * Spring's Page object into a simpler format.
 *
 * @param <T> the type of elements in the content list
 */
public record PaginatedResponse<T>(
    List<T> content, int page, int size, long totalElements, int totalPages) {

  /**
   * Creates a PaginatedResponse from a Spring Page object. Extracts pagination metadata and content
   * from the provided page.
   *
   * @param pageData the Spring Page object to convert .
   */
  public PaginatedResponse(Page<T> pageData) {
    this(
        pageData.getContent(),
        pageData.getNumber(),
        pageData.getSize(),
        pageData.getTotalElements(),
        pageData.getTotalPages());
  }
}
