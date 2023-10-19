package com.kasemodel.goldenraspberryaward.infra.persistence.entity;


import com.kasemodel.goldenraspberryaward.infra.model.InitialDataVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(indexes = {
	@Index(name = "award_deleted_at_idx", columnList = "deleted_at")
})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Award extends BaseEntity<Long> {
	@Column(name = "released_year", nullable = false)
	private Short year;
	@ManyToOne(targetEntity = Movie.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private Movie movie;
	@Column(name = "winner", nullable = false)
	private Boolean winner;

	private Award(final Short year, final Movie movie, final Boolean winner) {
		this.year = year;
		this.movie = movie;
		this.winner = Boolean.TRUE.equals(winner);
		setExternalId(UUID.randomUUID());
	}

	public static Award of(final InitialDataVO dataVO, final Movie movie) {
		return new Award(dataVO.getYear(), movie, dataVO.getWinner());
	}

//	public static Award of(final InitialDataVO initialDataVO) {
//		return new Award(initialDataVO.getYear(), Movie.of(initialDataVO), initialDataVO.getWinner());
//	}
}
