package com.kasemodel.goldenraspberryaward.infra.persistence.repository;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import org.springframework.data.repository.CrudRepository;

public interface AwardRepository extends CrudRepository<Award, Long> {
}
