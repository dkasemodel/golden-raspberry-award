package com.kasemodel.goldenraspberryaward.infra.persistence.repository;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProducerRepository extends CrudRepository<Producer, Long> {
	Optional<Producer> findByName(String name);

	@Query(
		value = "with"
			+ "    winners as ( "
			+ "      select p.id as producer_id "
			+ "           , p.name as producer_name "
			+ "           , count(a.id) "
			+ "      from producer as p "
			+ "      join producer_movie as pm on (pm.producer_id = p.id) "
			+ "      join movie as m on (pm.movie_id = m.id and m.deleted_at is null) "
			+ "      join award as a on (a.movie_id = m.id and a.deleted_at is null) "
			+ "      where a.winner "
			+ "        and p.deleted_at is null "
			+ "      group by p.id, p.name "
			+ "      having count(a.id) > 1) "
			+ "  select w.producer_name "
			+ "       , a.released_year "
			+ "  from winners as w "
			+ "  join producer_movie as pm on (pm.producer_id = w.producer_id) "
			+ "  join award as a on (pm.movie_id = a.movie_id and a.deleted_at is null) "
			+ "  where a.winner",
		nativeQuery = true)
	Optional<List<Tuple>> findWinners();

	Optional<Producer> findByExternalIdAndDeletedAtIsNull(UUID externalId);

	boolean existsByName(String name);

	@Transactional(Transactional.TxType.REQUIRED)
	@Modifying
	@Query(value = "update producer set deleted_at = now() where external_id = :externalId",
		nativeQuery = true)
	void deleteByExternalId(@Param("externalId") UUID externalId);

	Optional<List<Producer>> findAllByDeletedAtIsNull();
}
