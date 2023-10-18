package com.kasemodel.goldenraspberryaward.interfaces.rest.model;

import lombok.Getter;

import java.util.UUID;

public record ProducerResponse (UUID externalId, String name) {}
