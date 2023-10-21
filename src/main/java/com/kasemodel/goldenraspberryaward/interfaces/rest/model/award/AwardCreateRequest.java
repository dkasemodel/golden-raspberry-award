package com.kasemodel.goldenraspberryaward.interfaces.rest.model.award;

import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ExternalIdRequest;

public record AwardCreateRequest(Short year, Boolean winner,
								 ExternalIdRequest movie) {
}
