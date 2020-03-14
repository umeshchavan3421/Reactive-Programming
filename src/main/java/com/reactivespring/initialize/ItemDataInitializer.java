package com.reactivespring.initialize;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.mongodb.Mongo;
import com.reactivespring.controller.v1.ItemController;
import com.reactivespring.document.Item;
import com.reactivespring.document.ItemCapped;
import com.reactivespring.repository.ItemReactiveCappedRepository;
import com.reactivespring.repository.ItemReactiveRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ItemDataInitializer implements CommandLineRunner{

	@Autowired
	private ItemReactiveRepository repo;
	
	@Autowired
	private ItemReactiveCappedRepository capRepo;
	
	@Autowired
	MongoOperations mongoOperations;
	
	@Override
	public void run(String... args) throws Exception {

		initialDataSetup();
		
		createCappedCollections();
		
		dataSetupForCappedCollection();
	}
	
	private void createCappedCollections() {

		mongoOperations.dropCollection(ItemCapped.class);
		
		mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
		
	}
	
	public void dataSetupForCappedCollection() {
		Flux<ItemCapped> itemcappedFlux =  Flux.interval(Duration.ofSeconds(1))
		.map(i -> new ItemCapped(null, "Random Item" +i, (100 +i) ));
		
		capRepo.insert(itemcappedFlux)
		.subscribe((itemcapped) -> {
			
			log.info("Inserted item is:" +itemcapped);
			
		});
	}

	public List<Item> data(){
		return Arrays.asList(new Item("mobile","Samsung", 25000),
				new Item(null,"Iphone", 45000),
				new Item(null,"MI", 25));
	}

	private void initialDataSetup() {

		repo.deleteAll()
		.thenMany(Flux.fromIterable(data()))
		.flatMap(repo::save)
		.thenMany(repo.findAll())
		.subscribe(item -> {
			System.out.println("Item inserted" +item);
		});
	}

}
