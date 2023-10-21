package com.kasemodel.goldenraspberryaward.interfaces.rest.model;

import java.util.Set;

public record CreateMovieRequest(String title, Set<ProducerResponse> producers,
								 Set<StudioResponse> studios) {
}
