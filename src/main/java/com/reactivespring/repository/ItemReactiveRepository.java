package com.reactivespring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.reactivespring.document.Item;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String>{

}
