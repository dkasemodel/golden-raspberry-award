package com.kasemodel.goldenraspberryaward.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Entity
@Table(indexes = {
	@Index(name = "producer_deleted_at_idx", columnList = "deleted_at"),
	@Index(name = "producer_external_id_idx", columnList = "external_id")
})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Producer extends BaseEntity<Long> {
	@Column(name = "name", length = 255, nullable = false, unique = true)
	private String name;

	private Producer(final String name) {
		this.name = StringUtils.trim(name);
		setExternalId(UUID.randomUUID());
	}

	public static Producer of(final String name) {
		return new Producer(name);
	}
}
