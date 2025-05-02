package com.skthvl.storeapi.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class StoreTest {

  @Test
  void testIsOpenIsClosed_ShouldReturnFalse_WhenTodayOpenIsNull() {
    final Store store = Store.builder().todayClose(LocalTime.of(18, 0)).build();

    assertFalse(store.isOpen());
    assertTrue(store.isClosed());
  }

  @Test
  void testIsOpenIsClosed_ShouldReturnFalse_WhenTodayCloseIsNull() {
    final Store store = Store.builder().todayOpen(LocalTime.of(9, 0)).build();

    assertFalse(store.isOpen());
    assertTrue(store.isClosed());
  }

  @Test
  void testIsOpenIsClosed_ShouldReturnFalse_WhenCurrentTimeIsBeforeTodayOpen() {
    final Store store =
        Store.builder()
            .todayOpen(LocalTime.now().plusHours(1))
            .todayClose(LocalTime.now().plusHours(3))
            .build();

    assertFalse(store.isOpen());
    assertTrue(store.isClosed());
  }

  @Test
  void testIsOpenIsClosed_ShouldReturnFalse_WhenCurrentTimeIsAfterTodayClose() {
    final Store store =
        Store.builder()
            .todayOpen(LocalTime.now().minusHours(2))
            .todayClose(LocalTime.now().minusHours(1))
            .build();

    assertFalse(store.isOpen());
    assertTrue(store.isClosed());
  }

  @Test
  void testIsOpenIsClosed_ShouldReturnTrue_WhenCurrentTimeIsWithinOperatingHours() {
    final Store store =
        Store.builder()
            .todayOpen(LocalTime.now().minusHours(1))
            .todayClose(LocalTime.now().plusHours(1))
            .build();

    assertTrue(store.isOpen());
    assertFalse(store.isClosed());
  }
}
