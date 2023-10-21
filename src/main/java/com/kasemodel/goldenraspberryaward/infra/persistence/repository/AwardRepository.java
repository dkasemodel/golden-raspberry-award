package com.kasemodel.goldenraspberryaward.infra.persistence.repository;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AwardRepository extends CrudRepository<Award, Long> {
	boolean existsByYearAndMovieExternalId(Short year, UUID movieExternalId);

	Optional<Award> findByExternalId(UUID externalId);

	@Query("SELECT award"
		+ " FROM Award award"
		+ " JOIN FETCH award.movie movie"
		+ " JOIN FETCH movie.producers producer"
		+ " JOIN FETCH movie.studios studio"
		+ " WHERE award.externalId = :externalId"
		+ "   AND movie.deletedAt IS NULL"
		+ "   AND producer.deletedAt IS NULL"
		+ "   AND studio.deletedAt IS NULL")
	Optional<Award> findFetchByExternalId(UUID externalId);

	@Query(value = "SELECT award"
		+ " FROM Award award"
		+ " JOIN FETCH award.movie movie"
		+ " WHERE movie.deletedAt IS NULL",
		countQuery = "SELECT COUNT(1) FROM Award a WHERE a.deletedAt IS NULL")
	Page<Award> findAll(PageRequest pageable);

	boolean existsByExternalId(UUID externalId);

	@Transactional(Transactional.TxType.REQUIRED)
	@Modifying
	@Query(value = "update award set deleted_at = now() where external_id = :externalId",
		nativeQuery = true)
	void deleteByExternalId(UUID externalId);

	@Query("SELECT award.movie"
		+ " FROM Award award"
		+ " WHERE award.externalId = :externalId"
		+ "   AND award.deletedAt IS NULL")
	Optional<Movie> findMovieByExternalId(UUID externalId);
}
