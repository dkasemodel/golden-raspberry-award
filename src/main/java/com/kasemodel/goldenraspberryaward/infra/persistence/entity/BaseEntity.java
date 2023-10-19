package com.kasemodel.goldenraspberryaward.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity<T> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private T id;
	@Column(name = "external_id", updatable = false)
	private UUID externalId;
	@Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "timestamp default now()")
	private LocalDateTime createdAt;
	@Column(name = "deleted_at", insertable = false)
	private LocalDateTime deletedAt;
}
