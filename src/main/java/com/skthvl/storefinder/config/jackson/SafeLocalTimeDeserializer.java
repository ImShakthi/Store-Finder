package com.skthvl.storefinder.config.jackson;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom JSON deserializer for LocalTime values that handles special cases like null, blank, or "GESLOTEN" values.
 * Uses "HH:mm" format for time parsing.
 */
@Slf4j
public class SafeLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Deserializes JSON string to LocalTime object.
   *
   * @param jsonParser parser used for reading JSON content
   * @param ctxt context for deserialization
   * @return LocalTime object or null if input is null, blank, or "GESLOTEN"
   * @throws IOException if there is a problem reading from JsonParser
   */
  @Override
  public LocalTime deserialize(final JsonParser jsonParser, final DeserializationContext ctxt)
      throws IOException {
    final String value = jsonParser.getText();
    if (isNull(value) || value.isBlank() || value.equalsIgnoreCase("GESLOTEN")) {
      return null;
    }
    log.info(">>>> value: {}", value);
    return LocalTime.parse(value, FORMATTER);
  }
}
