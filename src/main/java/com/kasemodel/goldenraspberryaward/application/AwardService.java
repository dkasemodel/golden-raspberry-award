package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;

public interface AwardService {
	Award create(Award award);

	Award save(Award award);
}
