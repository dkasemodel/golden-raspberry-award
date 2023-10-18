package com.kasemodel.goldenraspberryaward.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}
