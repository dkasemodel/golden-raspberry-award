package com.kasemodel.goldenraspberryaward.interfaces.rest.controller.studio;

import com.google.gson.Gson;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.kasemodel.goldenraspberryaward.utils.TestUtils.UUID_REGEX_PATTERN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StudioControllerTest {
	private static final String ROOT_PATH = "/v1/studios";
	private static final Gson gson = new Gson();
	private static final String STUDIO_NAME = "New Studio Name";
	private static final String DUPLICATED_STUDIO_NAME = "Universal Studios";
	private static final String DUPLICATED_STUDIO_ERROR = "Producer "+ DUPLICATED_STUDIO_NAME + " already exists";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCreateANewStudio_whenDoAPost() throws Exception {
		final var mvcResult = mockMvc.perform(post(ROOT_PATH)
				.content(gson.toJson(buildCreateByNameRequest()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();

		final var resultLocation = mvcResult.getResponse().getHeader("location");
		assertThat(resultLocation, notNullValue());
		final var resultLocationRoot = resultLocation.substring(0, resultLocation.lastIndexOf("/"));
		final var resultLocationUUID = resultLocation.substring(resultLocation.lastIndexOf("/") + 1);
		assertThat(resultLocationRoot, equalTo(ROOT_PATH));
		assertThat(resultLocationUUID, matchesRegex(UUID_REGEX_PATTERN));
	}

	@Test
	void shouldReturnABadRequest_whenDoAPost() throws Exception {
		final var mvcResult = mockMvc.perform(post(ROOT_PATH)
				.content(gson.toJson(buildDuplicatedCreateByNameRequest()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(DUPLICATED_STUDIO_ERROR))
			.andReturn();
	}

	private CreateByNameRequest buildDuplicatedCreateByNameRequest() {
		return new CreateByNameRequest(DUPLICATED_STUDIO_NAME);
	}

	private CreateByNameRequest buildCreateByNameRequest() {
		return new CreateByNameRequest(STUDIO_NAME);
	}
}
