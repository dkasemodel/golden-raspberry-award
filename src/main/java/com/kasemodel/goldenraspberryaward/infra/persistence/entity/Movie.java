package com.kasemodel.goldenraspberryaward.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(indexes = {
	@Index(name = "movie_deleted_at_idx", columnList = "deleted_at"),
	@Index(name = "movie_title_idx", columnList = "title"),
	@Index(name = "movie_external_id_idx", columnList = "external_id")
})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Movie extends BaseEntity<Long> {
	@Column(name = "title", nullable = false, length = 255, unique = true)
	private String title;
	@ManyToMany(targetEntity = Producer.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(
		name = "producer_movie",
		joinColumns = @JoinColumn(name = "movie_id"),
		inverseJoinColumns = @JoinColumn(name = "producer_id")
	)
	private Set<Producer> producers;
	@ManyToMany(targetEntity = Studio.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(
		name = "movie_studio",
		joinColumns = @JoinColumn(name = "movie_id"),
		inverseJoinColumns = @JoinColumn(name = "studio_id")
	)
	private Set<Studio> studios;

	private Movie(final String title, final Set<Producer> producers, final Set<Studio> studios) {
		this.title = title;
		this.producers = producers;
		this.studios = studios;
		setExternalId(UUID.randomUUID());
	}

//	public static Movie of(final InitialDataVO initialDataVO) {
//		return new Movie(
//			initialDataVO.getTitle(),
//			initialDataVO.getProducers().stream().map(Producer::of).collect(Collectors.toSet()),
//			initialDataVO.getStudios().stream().map(Studio::of).collect(Collectors.toSet())
//		);
//	}

	public static Movie of(String title, Set<Producer> producers, Set<Studio> studios) {
		return new Movie(title, producers, studios);
	}
}
