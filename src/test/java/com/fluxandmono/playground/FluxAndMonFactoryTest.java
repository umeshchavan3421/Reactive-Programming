package com.fluxandmono.playground;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = FluxAndMonFactoryTest.class)
public class FluxAndMonFactoryTest {
	
	List<String> names = Arrays.asList("Umesh","Ramesh","Yash","Raj");
	
	@Test
	public void fluxUsingIterableTest() {
		Flux<String> fluxOfString =  Flux.fromIterable(names);
		
		StepVerifier.create(fluxOfString.log())
		.expectNext("Umesh","Ramesh","Yash","Raj")
		.verifyComplete();
	
	}
	
	@Test
	public void fluxFrom_ArrayTest() {
		String[] names = new String[] {"Umesh","Ramesh","Yash","Raj"};
		
		Flux<String> fluxOfStringFromArray =  Flux.fromArray(names);
		
		StepVerifier.create(fluxOfStringFromArray.log())
		.expectNext("Umesh","Ramesh","Yash","Raj")
		.verifyComplete();
	
	}
	
	@Test
	public void fluxFrom_StreamTest() {
		
		//Here for streams terminal operator is verify method
		
		Flux<String> fluxOfStringFromStream =  Flux.fromStream(names.stream());
		
		StepVerifier.create(fluxOfStringFromStream.log())
		.expectNext("Umesh","Ramesh","Yash","Raj")
		.verifyComplete();
	
	}
	
	@Test
	public void flux_Using_Range() {
		
		Flux<Integer> fluxOfInteger =  Flux.range(1, 5);
		
		
		StepVerifier.create(fluxOfInteger.log())
		.expectNext(1,2,3,4,5)
		.verifyComplete();
	
	}

	
	@Test
	public void mono_using_just_empty() {
		
		//No data to emit so only verify its completion
		Mono<String> mono = Mono.justOrEmpty(null);
		
		StepVerifier.create(mono.log())
		.verifyComplete();
	
	}
	
	@Test
	public void mono_using_supplier() {
		
		Supplier<String> stringSupplier = () -> "Umesh";
		Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
		
		System.out.println(stringSupplier.get());
		
		StepVerifier.create(stringMono.log())
		.expectNext("Umesh")
		.verifyComplete();
	
	}

}
