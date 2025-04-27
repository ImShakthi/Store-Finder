package com.skthvl.storefinder.model.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
    List<T> content, int page, int size, long totalElements, int totalPages) {
  public PaginatedResponse(Page<T> pageData) {
    this(
        pageData.getContent(),
        pageData.getNumber(),
        pageData.getSize(),
        pageData.getTotalElements(),
        pageData.getTotalPages());
  }
}
