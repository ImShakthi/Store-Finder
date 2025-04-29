package com.skthvl.storefinder.util;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/** Utility class for handling Spring Data Page objects and their transformations. */
public class PageUtils {
  /**
   * Maps content of a source Page to a new Page using the provided mapping function.
   *
   * @param <D> source type
   * @param <T> target type
   * @param sourcePage the source Page to map from
   * @param mapper the function to map source elements to target elements
   * @return a new Page containing mapped elements
   */
  public static <D, T> Page<T> mapPage(final Page<D> sourcePage, final Function<D, T> mapper) {
    final List<T> mappedContent = sourcePage.getContent().stream().map(mapper).toList();

    return new PageImpl<>(mappedContent, sourcePage.getPageable(), sourcePage.getTotalElements());
  }
}
