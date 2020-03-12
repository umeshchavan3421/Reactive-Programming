package com.fluxandmono.playground;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformTest {

	List<String> names = Arrays.asList("Umesh", "Ramesh", "Yash", "Raj");

	@Test
	public void transformUsingMap() {

		Flux<String> namesFlux = Flux.fromIterable(names).map(s -> s.toUpperCase()).log();

		StepVerifier.create(namesFlux.log()).expectNext("UMESH", "RAMESH", "YASH", "RAJ").verifyComplete();

	}

	@Test
	public void transformUsingMapLenght() {

		Flux<Integer> namesFlux = Flux.fromIterable(names).map(s -> s.length()).log();

		StepVerifier.create(namesFlux.log()).expectNext(5, 6, 4, 3).verifyComplete();

	}

	@Test
	public void transformUsingMapLenght_Repeat() {

		Flux<Integer> namesFlux = Flux.fromIterable(names).map(s -> s.length()).repeat(1).log();

		StepVerifier.create(namesFlux.log()).expectNext(5, 6, 4, 3,5, 6, 4, 3).verifyComplete();

	}
	
	@Test
	public void transformUsingMapFilter() {

		Flux<String> namesFlux = Flux.fromIterable(names)
				.filter(s -> s.length() > 4)
				.map(s -> s.toUpperCase()).log();

		StepVerifier.create(namesFlux.log()).expectNext("UMESH", "RAMESH").verifyComplete();

	}
	

	@Test
	public void transformUsingFlatMap() {

		Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
				.flatMap(s -> {
					return Flux.fromIterable(convertToList(s));
				}).log();
				
		StepVerifier.create(namesFlux.log())
		.expectNextCount(12)
		.verifyComplete();

	}
	
	/*@Test
	public void transformUsingFlatMapParallel() {

		Flux<String> namesFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
				.window(2) //This will be flux of string
				.flatMap((s) -> 
					s.map(this::convertToList).subscribeOn(parallel()))
					.flatMap(s -> Flux.fromIterable(s))
				
				.log();
				
		StepVerifier.create(namesFlux.log())
		.expectNextCount(12)
		.verifyComplete();

	}*/

	private List<String> convertToList(String s)  {
		// TODO Auto-generated method stub
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Arrays.asList(s, "newValue");
	}

}
