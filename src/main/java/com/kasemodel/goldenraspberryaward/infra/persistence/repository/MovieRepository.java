package com.kasemodel.goldenraspberryaward.infra.persistence.repository;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
	Optional<Movie> findByTitle(String title);

	boolean existsByTitle(String title);

	@Query("SELECT movie"
		+ " FROM Movie movie"
		+ " JOIN FETCH movie.producers as producer"
		+ " JOIN FETCH movie.studios as studio"
		+ " WHERE movie.externalId = :externalId"
		+ "   AND movie.deletedAt IS NULL"
		+ "   AND producer.deletedAt IS NULL"
		+ "   AND studio.deletedAt IS NULL")
	Optional<Movie> findFetchByExternalIdAndDeletedAtIsNull(UUID externalId);

	@Query(value = "SELECT movie"
		+ " FROM Movie movie"
		+ " JOIN FETCH movie.producers as producer"
		+ " JOIN FETCH movie.studios as studio"
		+ " WHERE movie.deletedAt IS NULL"
		+ "   AND producer.deletedAt IS NULL"
		+ "   AND studio.deletedAt IS NULL",
		countQuery = "SELECT COUNT(1) FROM Movie m WHERE m.deletedAt IS NULL")
	Page<Movie> findAll(Pageable pageable);

	@Transactional(Transactional.TxType.REQUIRED)
	@Modifying
	@Query(value = "update movie set deleted_at = now() where external_id = :externalId",
		nativeQuery = true)
	void deleteByExternalId(UUID externalId);

	Optional<Movie> findByExternalId(UUID externalId);

	@Query("SELECT movie.producers"
		+ " FROM Movie movie"
		+ " WHERE movie.externalId = :movieExternalId")
	Optional<List<Producer>> findAllProducersByExternalId(UUID movieExternalId);

	@Query("SELECT movie.studios"
		+ " FROM Movie movie"
		+ " WHERE movie.externalId = :movieExternalId")
	Optional<List<Studio>> findAllStudiosByExternalId(UUID movieExternalId);

	boolean existsByExternalId(UUID externalId);
}
