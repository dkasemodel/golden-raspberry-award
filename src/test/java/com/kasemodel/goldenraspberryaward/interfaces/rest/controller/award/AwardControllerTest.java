package com.kasemodel.goldenraspberryaward.interfaces.rest.controller.award;

import com.google.gson.Gson;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardWinnersResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AwardControllerTest {
	private static final String ROOT_PATH = "/v1/awards";
	private static final String WINNERS_PATH = ROOT_PATH + "/producers/winners";
	private static final Gson gson = new Gson();
	private static final String PRODUCER_MIN_WINNER = "Joel Silver";
	private static final Integer INTERVAL_MIN_WINNER = 1;
	private static final Integer PREVIOUS_WIN_MIN_WINNER = 1990;
	private static final Integer FOLLOWING_WIN_MIN_WINNER = 1991;
	private static final String PRODUCER_MAX_WINNER = "Matthew Vaughn";
	private static final Integer INTERVAL_MAX_WINNER = 13;
	private static final Integer PREVIOUS_WIN_MAX_WINNER = 2002;
	private static final Integer FOLLOWING_WIN_MAX_WINNER = 2015;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnAListOfOneWinnerEach() throws Exception {
		final MvcResult result = mockMvc.perform(get(WINNERS_PATH)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertThat(result.getResponse().getContentAsString(), notNullValue());
		final var resultObject = gson.fromJson(result.getResponse().getContentAsString(), AwardWinnersResponse.class);
		assertThat(resultObject.min(), hasSize(1));
		assertThat(resultObject.max(), hasSize(1));
		final var minWinner = resultObject.min().get(0);
		assertThat(minWinner.getProducer(), equalTo(PRODUCER_MIN_WINNER));
		assertThat(minWinner.getInterval(), equalTo(INTERVAL_MIN_WINNER));
		assertThat(minWinner.getPreviousWin(), equalTo(PREVIOUS_WIN_MIN_WINNER));
		assertThat(minWinner.getFollowingWin(), equalTo(FOLLOWING_WIN_MIN_WINNER));
		final var maxWinner = resultObject.max().get(0);
		assertThat(maxWinner.getProducer(), equalTo(PRODUCER_MAX_WINNER));
		assertThat(maxWinner.getInterval(), equalTo(INTERVAL_MAX_WINNER));
		assertThat(maxWinner.getPreviousWin(), equalTo(PREVIOUS_WIN_MAX_WINNER));
		assertThat(maxWinner.getFollowingWin(), equalTo(FOLLOWING_WIN_MAX_WINNER));
	}
}
