package com.skthvl.storeapi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class PageUtilsTest {

  @Test
  void testMapPageWithValidInput() {
    // Given
    final List<String> sourceContent = Arrays.asList("1", "2", "3");
    final Pageable pageable = mock(Pageable.class);
    final Page<String> sourcePage = new PageImpl<>(sourceContent, pageable, sourceContent.size());

    // When
    final Page<Integer> resultPage = PageUtils.mapPage(sourcePage, Integer::valueOf);

    // Then
    List<Integer> expectedContent = Arrays.asList(1, 2, 3);
    assertEquals(expectedContent, resultPage.getContent());
    assertEquals(sourcePage.getTotalElements(), resultPage.getTotalElements());
    assertEquals(sourcePage.getPageable(), resultPage.getPageable());
  }

  @Test
  void testMapPageWithEmptySourcePage() {
    // Given
    final List<String> sourceContent = List.of();
    final Pageable pageable = mock(Pageable.class);
    final Page<String> sourcePage = new PageImpl<>(sourceContent, pageable, 0);

    // When
    final Page<Integer> resultPage = PageUtils.mapPage(sourcePage, Integer::valueOf);

    // Then
    assertEquals(0, resultPage.getContent().size());
    assertEquals(sourcePage.getTotalElements(), resultPage.getTotalElements());
    assertEquals(sourcePage.getPageable(), resultPage.getPageable());
  }

  @Test
  void testMapPageHandlesNullInSourceContent() {
    // Given
    final List<String> sourceContent = Arrays.asList("1", null, "3");
    final Pageable pageable = mock(Pageable.class);
    final Page<String> sourcePage = new PageImpl<>(sourceContent, pageable, sourceContent.size());

    final Function<String, String> mapper = value -> value == null ? "null" : value;

    // When
    final Page<String> resultPage = PageUtils.mapPage(sourcePage, mapper);

    // Then
    List<String> expectedContent = Arrays.asList("1", "null", "3");
    assertEquals(expectedContent, resultPage.getContent());
    assertEquals(sourcePage.getTotalElements(), resultPage.getTotalElements());
    assertEquals(sourcePage.getPageable(), resultPage.getPageable());
  }
}
