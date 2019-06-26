package com.springreactivelearning.controller.v1;

import com.springreactivelearning.document.ItemCapped;
import com.springreactivelearning.repository.ItemCappedReactiveRepository;
import com.springreactivelearning.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.springreactivelearning.constants.ItemConstants.ITEM_STREAM_END_POINT_V1;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemStreamControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemCappedReactiveRepository itemCappedReactiveRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Before
    public void setUp(){
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(5000).capped());

        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map( i -> new ItemCapped(null, "Random Item" + i, 100.0 + i))
                .take(3)
                .log();

        itemCappedReactiveRepository.insert(itemCappedFlux)
                .doOnNext(res -> System.out.println("Inserted item is "+ res.getDescription()))
        .blockLast();
    }

    @Test
    public void streamTest(){
        Flux<ItemCapped> itemCappedFlux = webTestClient.get().uri(ITEM_STREAM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ItemCapped.class)
                .getResponseBody()
                .take(5);

        StepVerifier.create(itemCappedFlux)
                .expectNextCount(3)
                .thenCancel()
                .verify();
    }
}
