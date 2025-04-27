package com.skthvl.storefinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract base class for entities that require creation and modification timestamp auditing.
 * Automatically manages createdAt and updatedAt fields using JPA entity listeners.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
  /** Timestamp when the entity was created. Cannot be updated once set. */
  @CreatedDate
  @Column(updatable = false, nullable = false)
  protected Instant createdAt;

  /** Timestamp when the entity was last modified. Automatically updated on each save. */
  @LastModifiedDate
  @Column(nullable = false)
  protected Instant updatedAt;
}
