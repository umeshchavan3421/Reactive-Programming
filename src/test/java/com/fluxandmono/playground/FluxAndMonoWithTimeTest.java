package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoWithTimeTest {
	
	@Test
	public void infiniteSequence() throws InterruptedException {
		
		//After every 200 mili sec it will emit flux of long
		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
				.log();
		
		infiniteFlux
		.subscribe((element) -> System.out.println(element));
		
		Thread.sleep(3000);
		
	}
	
	@Test
	public void infiniteSequenceTest() throws InterruptedException {
		
		//After every 200 mili sec it will emit flux of long
		Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
				.take(3)
				.log();
		
		StepVerifier.create(finiteFlux.log())
		.expectSubscription()
		.expectNext(0L,1L,2L)
		.verifyComplete();
		
	}
	
	@Test
	public void infiniteSequenceMap() throws InterruptedException {
		
		//After every 200 mili sec it will emit flux of long
		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
				.map(l -> new Integer(l.intValue()))
				.take(3)
				.log();
		
		StepVerifier.create(finiteFlux)
		.expectSubscription()
		.expectNext(0,1,2)
		.verifyComplete();
		
	}
	
	@Test
	public void infiniteSequenceMap_WithDelay() throws InterruptedException {
		
		//After every 200 mili sec it will emit flux of long
		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
				.delayElements(Duration.ofSeconds(1))
				.map(l -> new Integer(l.intValue()))
				.take(3)
				.log();
		
		StepVerifier.create(finiteFlux)
		.expectSubscription()
		.expectNext(0,1,2)
		.verifyComplete();
		
	}

}
