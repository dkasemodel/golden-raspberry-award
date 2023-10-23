package com.kasemodel.goldenraspberryaward.infra.persistence.repository;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudioRepository extends CrudRepository<Studio, Long> {
	Optional<Studio> findByName(String name);

	boolean existsByName(String name);

	Optional<Studio> findByExternalIdAndDeletedAtIsNull(UUID externalId);

	Page<Studio> findAllByDeletedAtIsNull(PageRequest pageable);

	@Transactional(Transactional.TxType.REQUIRED)
	@Modifying
	@Query(value = "update studio set deleted_at = now() where external_id = :externalId",
		nativeQuery = true)
	void deleteByExternalId(@Param("externalId") UUID externalId);

	boolean existsByExternalId(UUID externalId);
}
