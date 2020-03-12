package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

//@RunWith(SpringRunner.class)
//@WebFluxTest
@AutoConfigureWebTestClient
@SpringBootTest(classes = FluxAndMonoController.class)
public class FluxAndMonoControllerTest {
	
	@MockBean
	private WebTestClient webTestClient;
	
	@InjectMocks
	FluxAndMonoController fluxAndMonoController;
	
	
	@Test
	public void returnFluxTest()  {
		Flux<Integer> intFlux =  webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange().expectStatus().isOk().returnResult(Integer.class)
		.getResponseBody();
		
		StepVerifier.create(intFlux)
		.expectSubscription()
		.expectNext(1)
		.expectNext(2)
		.expectNext(3)
		.expectNext(4)
		.verifyComplete();
		
	}
	
		

}
