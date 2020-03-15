package com.reactivespring.controller.v1;

import java.time.Duration;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.document.Item;
import com.reactivespring.document.Topology;
import com.reactivespring.document.TopologyMoasicResponse;
import com.reactivespring.repository.ItemReactiveRepository;
import com.reactivespring.repository.TopologyRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {
	
	@Autowired
	private ItemReactiveRepository repository;
	
	@Autowired
	private TopologyRepository topologyrepo;
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
		log.error("Exception caught: {}" +ex );
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
	
	
	@GetMapping("/v1/items")
	public Flux<Item> getAllItems(){
		
		return repository.findAll().log();
		
	}
	
	@GetMapping("/v1/item/one/{id}")
	public Mono<ResponseEntity<Item>> getOneItems(@PathVariable String id){
		
		return repository.findById(id)
				.map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
	}
	
	@PostMapping("/v1/item/add")
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Item> createItem(@RequestBody Item item){
		
		return repository.save(item);
		
	}
	
	@DeleteMapping("/v1/item/delete")
	public Mono<Void> deleteItem(@RequestBody String id){
		
		return repository.deleteById(id);
		
	}
	
	@PutMapping("/v1/item/update/{id}")
	public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item){
		
		return repository.findById(id)
		.flatMap(currentItem -> {
			currentItem.setPrice(item.getPrice());
			currentItem.setDescription(item.getDescription());
			return repository.save(currentItem);
		})
		.map(updatedItem -> new ResponseEntity<>(updatedItem,HttpStatus.OK))
		.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		
	}
	
	
	@GetMapping("/v1/item/exception")
	public Flux<Item> runTimeException(@PathVariable String id){
		
		return repository.findAll()
				.concatWith(Mono.error(new RuntimeException("Runtime Exception Occured")));
		
	}
	
	
	@GetMapping("/v1/populate")
	public void populateData() throws InterruptedException{
		
		for(int i =0; i<=500 ; i++) {
			
			TopologyMoasicResponse mosaicresp = new TopologyMoasicResponse();
			
			mosaicresp.setSiteId("siteId" +i);
			
			mosaicresp.setCmtsName("cmts" +i);
			
			mosaicresp.setNodeName("node" +i);
			
			if(i == 100) {
				Thread.sleep(5000);
			}
			
			Flux<Topology> fluxOfTopology = Flux.just(mosaicresp)
					.delayElements(Duration.ofSeconds(3))
					.filter(Objects::nonNull).
					filter(element -> null != element.getSiteId())
					.map(mosaicTopology -> 
					new Topology(mosaicTopology.getSiteId(), mosaicTopology.getCmtsName(), mosaicTopology.getNodeName()));
			
			topologyrepo.insert(fluxOfTopology)
			.subscribe((itemcapped) -> {
				log.info("Inserted item is:" +itemcapped);
			});
		}
		
	}
	

	@GetMapping("/v1/get")
	public Flux<Topology> getData(){
		return topologyrepo.findAll().log();
		
	}

}
