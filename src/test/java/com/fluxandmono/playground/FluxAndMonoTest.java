package com.fluxandmono.playground;

import javax.management.RuntimeErrorException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = FluxAndMonoTest.class)
public class FluxAndMonoTest {
	
	@Test
	public void fluxTest() {
		Flux<String> stringFlux = Flux.just("Spring","Spring boot", "Reactive Spring").log();
		//The only way to access elements from flux is by subscribing
		stringFlux.subscribe(System.out::println); //This is simple subscribr
		
		
		//Attaching error to the flux
		Flux<String> stringFlux2 = Flux.just("Spring","Spring boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Flux with attached exception")))
				.concatWith(Flux.just("After error occured"))
				.log();
		//If any error occurs we have one more paramater in subscribe
		//Once the error is emmited from flux new all will not flow
		//The third parametr in subscribe is onComplete
		stringFlux2.subscribe(System.out::println,
				(e) -> System.err.println(e),
				() -> System.out.println("Completed"));
	}
	
	@Test
	public void fluxTestWithoutError() {
		Flux<String> stringFlux = Flux.just("Spring","Spring boot", "Reactive Spring").log();
		
		//STepVerifier Subscribs to flux then asserts the values of flux
		//If we don't add verifyComplete the flux won't flow as it is equivalent to subscribe Method
		StepVerifier.create(stringFlux)
		.expectNext("Spring")
		.expectNext("Spring boot")
		.expectNext("Reactive Spring")
		.verifyComplete();
		
	}
	
	
	@Test
	public void fluxTestWithError() {
		Flux<String> stringFlux = Flux.just("Spring","Spring boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Flux with attached exception")))
				.log();
		
		//STepVerifier Subscribs to flux then asserts the values of flux
		//If we don't add verifyComplete the flux won't flow as it is equivalent to subscribe Method
		StepVerifier.create(stringFlux)
		.expectNext("Spring")
		.expectNext("Spring boot")
		.expectNext("Reactive Spring")
		//We cannot have both expectError and ExpectErrorMessage
		//.expectError(RuntimeException.class)
		.expectErrorMessage("Flux with attached exception")
		.verify();
		
	}
	
	@Test
	public void fluxElementsCount_TestWithError() {
		Flux<String> stringFlux = Flux.just("Spring","Spring boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Flux with attached exception")))
				.log();
		
		//STepVerifier Subscribs to flux then asserts the values of flux
		//If we don't add verifyComplete the flux won't flow as it is equivalent to subscribe Method
		StepVerifier.create(stringFlux)
		.expectNextCount(3)
		.expectErrorMessage("Flux with attached exception")
		.verify();
		
	}
	
	@Test
	public void fluxTestWithError_SingleAssertLine() {
		Flux<String> stringFlux = Flux.just("Spring","Spring boot", "Reactive Spring")
				.concatWith(Flux.error(new RuntimeException("Flux with attached exception")))
				.log();
		
		//STepVerifier Subscribs to flux then asserts the values of flux
		//If we don't add verifyComplete the flux won't flow as it is equivalent to subscribe Method
		StepVerifier.create(stringFlux)
		.expectNext("Spring","Spring boot","Reactive Spring")
		//We cannot have both expectError and ExpectErrorMessage
		//.expectError(RuntimeException.class)
		.expectErrorMessage("Flux with attached exception")
		.verify();
		
	}
	
	@Test
	public void Mono_Test() {
		
		Mono<String> stringMono = Mono.just("Spring");
		
		StepVerifier.create(stringMono.log())
		.expectNext("Spring")
		.verifyComplete();
		
	}
	

	@Test
	public void Mono_With_Error_Test() {
		
		//Mono<String> stringMono = Mono.just("Spring");
		
		StepVerifier.create(Mono.error(new RuntimeException("Runetime error")).log())
		.expectError(RuntimeException.class)
		.verify();
		
	}


}
