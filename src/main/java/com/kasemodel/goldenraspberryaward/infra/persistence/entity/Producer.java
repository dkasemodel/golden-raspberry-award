package com.kasemodel.goldenraspberryaward.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Producer extends BaseEntity<Long> {
	@Column(name = "name", length = 255, nullable = false, unique = true)
	private String name;
//	@ManyToMany(targetEntity = Movie.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(
//		name = "producer_movie",
//		joinColumns = @JoinColumn(name = "producer_id"),
//		inverseJoinColumns = @JoinColumn(name = "movie_id")
//	)
//	private Set<Movie> movies;

	private Producer(final String name) {
		this.name = StringUtils.trim(name);
	}

	public static Producer of(final String name) {
		return new Producer(name);
	}
}
