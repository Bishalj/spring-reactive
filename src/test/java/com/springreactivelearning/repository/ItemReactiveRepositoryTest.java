package com.springreactivelearning.repository;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> items = Arrays.asList(
            new Item(null, "A", 1.0),
            new Item(null, "B", 2.0),
            new Item("3", "C", 3.0)
    );
    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items).log())
                .flatMap(itemReactiveRepository::save)
                .doOnNext((item) -> {
                    System.out.println(item);
                })
                .blockLast();
        ;
    }
    @Test
    public void getAllItem(){
        System.out.println(itemReactiveRepository);
        StepVerifier.create(itemReactiveRepository.findAll().log())
        .expectSubscription()
        .expectNextCount(3)
        .verifyComplete();
    }

    @Test
    public void getItemById(){
        StepVerifier.create(itemReactiveRepository.findById("3").log())
                .expectSubscription()
                .expectNextMatches( item -> item.getDescription().equals("C"))
                .verifyComplete();
    }

    @Test
    public void saveItem(){
        Item item = new Item(null,"D", 4.2);
        StepVerifier.create(itemReactiveRepository.save(item).log())
                .expectSubscription()
                .expectNextMatches( items -> items.getId() != null && items.getDescription().equals("D"))
                .verifyComplete();
    }

    @Test
    public void updateItem(){

        StepVerifier.create( itemReactiveRepository.findByDescription("C")
                .map( items -> {
                    items.setPrice(50.4);
                    return items;
                })
                .flatMap( items -> itemReactiveRepository.save(items)).log()
                )
                .expectSubscription()
                .expectNextMatches( items -> items.getPrice() == 50.4)
                .verifyComplete();
    }

    @Test
    public void deleteItem(){

        StepVerifier.create( itemReactiveRepository.findById("2")
                .map(Item::getId)
                .flatMap(id -> itemReactiveRepository.deleteById(id)).log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findById("2"))
                .expectSubscription()
                .verifyComplete();
    }
}
