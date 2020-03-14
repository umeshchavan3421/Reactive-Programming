package com.reactivespring.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactivespring.document.ItemCapped;
import com.reactivespring.repository.ItemReactiveCappedRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ItemStreamController {
	
	@Autowired
	private ItemReactiveCappedRepository repo;
	
	@GetMapping(value = "/stream/item", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<ItemCapped> getItemStream(){
		return repo.findItemsBy();
	}
 
}
