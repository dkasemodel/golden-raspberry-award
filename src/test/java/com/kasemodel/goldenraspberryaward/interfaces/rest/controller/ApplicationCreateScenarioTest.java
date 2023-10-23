package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.*;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardCreateRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardWinnersResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
	"award.initial-data.file=./src/test/resources/test-empty-data.csv",
	"award.initial-data.ignore-empty-data=true"
})
class ApplicationCreateScenarioTest {
	private static final String AWARD_ROOT_PATH = "/v1/awards";
	private static final String AWARD_PRODUCERS_WINNERS_PATH = AWARD_ROOT_PATH + "/producers/winners";
	private static final Gson gson = new Gson();
	private static final String STUDIOS_ROOT_PATH = "/v1/studios";
	private static final String STUDIO_NAME_PREFIX = "Studio_";
	private static final Integer STUDIOS_TO_CREATE = 10;
	private static final String PRODUCERS_ROOT_PATH = "/v1/producers";
	private static final String PRODUCER_NAME_PREFIX = "Producer_";
	private static final Integer PRODUCERS_TO_CREATE = 10;
	private static final String MOVIE_ROOT_PATH = "/v1/movies";
	private static final String MOVIE_TITLE_PREFIX = "Movie_";
	private static final Integer MOVIES_TO_CREATE = 10;
	private static final int MAX_PAGE_SIZE = 10;
	private static final List<Integer> AWARD_YEARS = List.of(
		1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989
	);

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldStartEmptyDataAppAndCreateAndValidate() throws Exception {
		createStudios();
		createProducers();
		final List<StudioResponse> studioList = validateCreatedStudios();
		final List<ProducerResponse> producerList = validateCreatedProducers();

		createMovies(studioList, producerList);
		final ArrayList<MovieResponse> movieList = validateCreatedMovie();

		final var producers = createAwards(movieList);
		validateAwardWinners(producers.getLeft(), producers.getRight());
	}

	private void createStudios() throws Exception {
		for (int i = 0; i < STUDIOS_TO_CREATE; i++) {
			createStudio(i);
		}
	}

	private void createStudio(final int studio) throws Exception {
		mockMvc.perform(post(STUDIOS_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(buildCreateStudio(studio))))
			.andExpect(status().isCreated())
			.andReturn();
	}

	private CreateByNameRequest buildCreateStudio(int studio) {
		return new CreateByNameRequest(STUDIO_NAME_PREFIX + studio);
	}

	private void createProducers() throws Exception {
		for (int i = 0; i < PRODUCERS_TO_CREATE; i++) {
			createProducer(i);
		}
	}

	private void createProducer(int producer) throws Exception {
		mockMvc.perform(post(PRODUCERS_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(buildCreateProducer(producer))))
			.andExpect(status().isCreated())
			.andReturn();
	}

	private CreateByNameRequest buildCreateProducer(int producer) {
		return new CreateByNameRequest(PRODUCER_NAME_PREFIX + producer);
	}

	private List<StudioResponse> validateCreatedStudios() throws Exception {
		final var mvcResult = mockMvc.perform(get(STUDIOS_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		final var resultContentString = mvcResult.getResponse().getContentAsString();
		assertThat(resultContentString, notNullValue());
		final var resultContent = gson.fromJson(resultContentString, PageResponse.class);
		assertThat(CollectionUtils.size(resultContent.content()), equalTo(STUDIOS_TO_CREATE));
		return (List<StudioResponse>) resultContent.content();
	}

	private List<ProducerResponse> validateCreatedProducers() throws Exception {
		final var mvcResult = mockMvc.perform(get(PRODUCERS_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		final var resultContentString = mvcResult.getResponse().getContentAsString();
		assertThat(resultContentString, notNullValue());
		final var resultContent = gson.fromJson(resultContentString, PageResponse.class);
		assertThat(CollectionUtils.size(resultContent.content()), equalTo(PRODUCERS_TO_CREATE));
		return (List<ProducerResponse>) resultContent.content();
	}

	private void createMovies(List<StudioResponse> studioList, List<ProducerResponse> producerList) throws Exception {
		for (int i = 0; i < MOVIES_TO_CREATE; i++) {
			final var studio = gson.fromJson(gson.toJson(studioList.get(i)), StudioResponse.class);
			final var producer = gson.fromJson(gson.toJson(producerList.get(i)), ProducerResponse.class);
			createMovie(i, studio, producer);
		}
	}

	private void createMovie(int movie, StudioResponse studioResponse, ProducerResponse producerResponse) throws Exception {
		mockMvc.perform(post(MOVIE_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(buildCreateMovie(movie, studioResponse, producerResponse))))
			.andExpect(status().isCreated())
			.andReturn();
	}

	private CreateMovieRequest buildCreateMovie(int movie, StudioResponse studioResponse, ProducerResponse producerResponse) {
		return new CreateMovieRequest(MOVIE_TITLE_PREFIX + movie, Set.of(producerResponse), Set.of(studioResponse));
	}

	private ArrayList<MovieResponse> validateCreatedMovie() throws Exception {
		final var mvcResult = mockMvc.perform(get(MOVIE_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		final var resultContentString = mvcResult.getResponse().getContentAsString();
		assertThat(resultContentString, notNullValue());
		final var pageResponse = gson.fromJson(resultContentString, PageResponse.class);
		assertThat(CollectionUtils.size(pageResponse.content()), equalTo(MOVIES_TO_CREATE));
		assertThat(pageResponse.page(), equalTo(1));
		assertThat(pageResponse.pageSize(), equalTo(MAX_PAGE_SIZE));
		assertThat(pageResponse.totalElements(), equalTo(MOVIES_TO_CREATE.longValue()));
		return (ArrayList<MovieResponse>) pageResponse.content();
	}

	private Pair<ProducerResponse, ProducerResponse> createAwards(List<MovieResponse> movieList) throws Exception {
		final MovieResponse movie1 = gson.fromJson(gson.toJson(movieList.get(0)), MovieResponse.class);
		final MovieResponse movie2 = gson.fromJson(gson.toJson(movieList.get(1)), MovieResponse.class);
		createAwardAsWinner(movie1, AWARD_YEARS.get(0));
		createAwardAsWinner(movie1, AWARD_YEARS.get(1));
		createAwardAsWinner(movie2, AWARD_YEARS.get(1));
		createAwardAsWinner(movie2, AWARD_YEARS.get(9));
		return Pair.of(
			movie1.producers().stream().findFirst().get(),
			movie2.producers().stream().findFirst().get());
	}

	private void createAwardAsWinner(MovieResponse movie, Integer year) throws Exception {
		mockMvc.perform(post(AWARD_ROOT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(buildAward(movie, year))))
			.andExpect(status().isCreated())
			.andReturn();
	}

	private AwardCreateRequest buildAward(MovieResponse movie, Integer year) {
		return new AwardCreateRequest(year.shortValue(), Boolean.TRUE, new ExternalIdRequest(movie.externalId()));
	}

	private void validateAwardWinners(ProducerResponse minWinner, ProducerResponse maxWinner) throws Exception {
		final var mvcResult = mockMvc.perform(get(AWARD_PRODUCERS_WINNERS_PATH)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		final var contentAsString = mvcResult.getResponse().getContentAsString();
		assertThat(contentAsString, notNullValue());
		final var awardWinnersResponse = gson.fromJson(contentAsString, AwardWinnersResponse.class);
		final var minResult = awardWinnersResponse.min();
		assertThat(CollectionUtils.size(minResult), equalTo(1));
		assertThat(minResult.get(0).getProducer(), equalTo(minWinner.name()));
		final var maxResult = awardWinnersResponse.max();
		assertThat(CollectionUtils.size(maxResult), equalTo(1));
		assertThat(maxResult.get(0).getProducer(), equalTo(maxWinner.name()));
	}
}
