package com.fluxandmono.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoFilterTest {
	
	List<String> names = Arrays.asList("Umesh","Ramesh","Yash","Raj");
	
	@Test
	public void filterTest() {
		
		Flux<String> fluxOfString =  Flux.fromIterable(names)
				.filter(s -> s.startsWith("U"))
				.log();
		
		StepVerifier.create(fluxOfString.log())
		.expectNext("Umesh")
		.verifyComplete();
	
	}
	
	@Test
	public void filterTestLength() {
		
		Flux<String> fluxOfString =  Flux.fromIterable(names)
				.filter(s -> s.length() > 4)
				.log();
		
		StepVerifier.create(fluxOfString.log())
		.expectNext("Umesh", "Ramesh")
		.verifyComplete();
	
	}

}
