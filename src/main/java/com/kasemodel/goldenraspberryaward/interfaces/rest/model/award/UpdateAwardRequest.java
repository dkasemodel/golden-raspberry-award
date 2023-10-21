package com.kasemodel.goldenraspberryaward.interfaces.rest.model.award;

import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ExternalIdRequest;

public record UpdateAwardRequest(Short year, Boolean winner,
								 ExternalIdRequest movie) {
}
