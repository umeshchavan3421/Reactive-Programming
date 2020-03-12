package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoCombinedTest {
	
	@Test
	public void combinedUsingMerge() {
		
		Flux<String> flux1 = Flux.just("A","B", "C").log();
		
		Flux<String> flux2 = Flux.just("D","E").log();
		
		Flux<String> mergedFlux = Flux.merge(flux1,flux2);
		
		StepVerifier.create(mergedFlux.log())
		.expectSubscription()
		.expectNext("A","B", "C","D","E")
		.verifyComplete();
	}
	
	@Test
	public void combinedUsingMerge_With_Deleay() {
		
		//Merge wont maontain the order so below test case may fail
		//Concate is the aternative
		Flux<String> flux1 = Flux.just("A","B", "C").delayElements(Duration.ofSeconds(1));
		
		Flux<String> flux2 = Flux.just("D","E").delayElements(Duration.ofSeconds(1));
		
		Flux<String> mergedFlux = Flux.merge(flux1,flux2);
		
		StepVerifier.create(mergedFlux.log())
		.expectSubscription()
		.expectNext("A","B","C","D","E")
		.verifyComplete();
	}
	

	@Test
	public void combinedUsingConcate_With_Deleay() {
		
		//Merge wont maontain the order
		//Concate is the aternative
		Flux<String> flux1 = Flux.just("A","B", "C").delayElements(Duration.ofSeconds(1));
		
		Flux<String> flux2 = Flux.just("D","E").delayElements(Duration.ofSeconds(1));
		
		Flux<String> mergedFlux = Flux.concat(flux1,flux2);
		
		StepVerifier.create(mergedFlux.log())
		.expectSubscription()
		.expectNext("A","B","C","D","E")
		.verifyComplete();
	}
	
	@Test
	public void combinedUsingZip_With_Deleay() {
		
		//Merge wont maontain the order
		//Concate is the aternative
		Flux<String> flux1 = Flux.just("A","B", "C").delayElements(Duration.ofSeconds(1));
		
		Flux<String> flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));
		
		Flux<String> mergedFlux = Flux.zip(flux1, flux2, (t1,t2) -> {
			return t1.concat(t2);
		}
		
				);
		
		StepVerifier.create(mergedFlux.log())
		.expectSubscription()
		.expectNext("AD","BE","CF")
		.verifyComplete();
	}


}
