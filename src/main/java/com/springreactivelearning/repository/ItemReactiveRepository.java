package com.springreactivelearning.repository;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    public Flux<Item> findByDescription(String description);

}
