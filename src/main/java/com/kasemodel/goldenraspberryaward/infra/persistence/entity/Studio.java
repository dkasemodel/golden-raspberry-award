package com.kasemodel.goldenraspberryaward.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Studio extends BaseEntity<Long> {
	@Column(name = "name", length = 255, nullable = false, unique = true)
	private String name;

	private Studio(final String name) {
		this.name = StringUtils.trim(name);
	}

	public static Studio of(final String name) {
		return new Studio(name);
	}
}
