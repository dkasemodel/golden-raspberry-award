package com.kasemodel.goldenraspberryaward.interfaces.rest.model;

import java.util.List;

public record PageResponse<T>(List<T> content, int page, int pageSize, int totalPages, long totalElements) {
}
