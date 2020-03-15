package com.reactivespring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.reactivespring.document.Topology;

public interface TopologyRepository extends ReactiveMongoRepository<Topology, String>{

}
