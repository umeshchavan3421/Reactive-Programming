package com.fluxandmono.playground;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoErrorTest {
	
	@Test
	public void fluxErrorHandling() {
		
		Flux<String> fluxOfString =  Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("Exception occured")))
				.concatWith(Flux.just("D"))
				.onErrorResume((e) -> {
					System.out.println("Exception is" + e);
					return Flux.just("Default", "Default1");
					});
		
		StepVerifier.create(fluxOfString.log())
		.expectSubscription()
		.expectNext("A","B","C")
		//.expectError(RuntimeException.class)
		.expectNext("Default","Default1")
		.verifyComplete();
	}
	
	@Test
	public void fluxErrorHandling_on_error_return() {
		
		Flux<String> fluxOfString =  Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("Exception occured")))
				.concatWith(Flux.just("D"))
				.onErrorReturn("Default");
		
		StepVerifier.create(fluxOfString.log())
		.expectSubscription()
		.expectNext("A","B","C")
		//.expectError(RuntimeException.class)
		.expectNext("Default")
		.verifyComplete();
	}
	
	@Test
	public void fluxErrorHandling_on_error_map() {
		
		Flux<String> fluxOfString =  Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("Exception occured")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e));
		
		StepVerifier.create(fluxOfString.log())
		.expectSubscription()
		.expectNext("A","B","C")
		.expectError(CustomException.class)
		.verify();
	}
	
	@Test
	public void fluxErrorHandling_on_error_map_With_Retry() {
		
		Flux<String> fluxOfString =  Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("Exception occured")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e))
				.retry(2);
		
		//Here before going to exception it will first flow the flux again for 2 times
		//As in first 2 go it will expect verifyComplete()
		
		StepVerifier.create(fluxOfString.log())
		.expectSubscription()
		.expectNext("A","B","C","A","B","C","A","B","C")
		.expectError(CustomException.class)
		.verify();
	}
	
	@Test
	public void fluxErrorHandling_on_error_map_With_BackOff() {
		
		
		//After rerty exhaust the illigal state exception will occur
		Flux<String> fluxOfString =  Flux.just("A","B","C")
				.concatWith(Flux.error(new RuntimeException("Exception occured")))
				.concatWith(Flux.just("D"))
				.onErrorMap((e) -> new CustomException(e))
				.retryBackoff(2, Duration.ofSeconds(5));
		
		//Here before going to exception it will first flow the flux again for 2 times
		//As in first 2 go it will expect verifyComplete()
		
		StepVerifier.create(fluxOfString.log())
		.expectSubscription()
		.expectNext("A","B","C","A","B","C","A","B","C")
		.expectError(IllegalStateException.class)
		.verify();
	}


}
