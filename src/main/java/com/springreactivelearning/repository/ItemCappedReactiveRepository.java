package com.springreactivelearning.repository;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.document.ItemCapped;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;


public interface ItemCappedReactiveRepository extends ReactiveMongoRepository<ItemCapped, String> {

    @Tailable
    Flux<ItemCapped> findItemsBy();
}
