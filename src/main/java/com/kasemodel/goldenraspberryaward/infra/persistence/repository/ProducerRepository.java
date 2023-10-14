package com.kasemodel.goldenraspberryaward.infra.persistence.repository;

import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;
import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerWithYearVO;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
			+ "      join movie as m on (pm.movie_id = m.id) "
			+ "      join award as a on (a.movie_id = m.id) "
			+ "      where a.winner "
			+ "      group by p.id, p.name "
			+ "      having count(a.id) > 1) "
			+ "  select w.producer_name "
			+ "       , a.released_year "
			+ "  from winners as w "
			+ "  join producer_movie as pm on (pm.producer_id = w.producer_id) "
			+ "  join award as a on (pm.movie_id = a.movie_id) "
			+ "  where a.winner",
		nativeQuery = true)
	Optional<List<Tuple>> findWinners();
}
