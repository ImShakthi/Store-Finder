package com.skthvl.storeapi.model.dto;

import lombok.Builder;

/** Record representing criteria for filtering stores based on various parameters. */
@Builder
public record StoreFilterCriteria(
    String city,
    Boolean openNow,
    Boolean collectionPoint,
    String storeLocationType,
    int page,
    int size) {}
