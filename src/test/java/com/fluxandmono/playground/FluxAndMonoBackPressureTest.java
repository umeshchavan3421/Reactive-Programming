package com.fluxandmono.playground;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {
	
	@Test
	public void backPressureTest() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10);
		
		StepVerifier.create(finiteFlux.log())
		.expectSubscription()
		.thenRequest(1)
		.expectNext(1)
		.thenRequest(1)
		.expectNext(2)
		.thenCancel()
		.verify();
		
	}
	
	@Test
	public void backPressure() {
		
		Flux<Integer> finiteFlux = Flux.range(1, 10);
		
		finiteFlux.subscribe((element) -> System.out.println(element)
				,(e) -> System.err.println("Exception occured" +e),
				() -> System.out.println("Completed flux"));
		
	}

}
