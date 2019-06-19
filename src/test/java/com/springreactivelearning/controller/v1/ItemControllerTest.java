package com.springreactivelearning.controller.v1;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.springreactivelearning.constants.ItemConstants.ITEM_END_POINT_V1;
import static junit.framework.TestCase.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item > data(){
        return Arrays.asList(
                new Item("554", "444", 33.3),
                new Item("563", "444", 33.3),
                new Item(null, "444", 33.3)
        );
    }

    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll().log()
                .thenMany(Flux.fromIterable(data())).log()
                .flatMap(itemReactiveRepository::save).log()
                .doOnNext(item -> System.out.println("The item is "+ item))
                .blockLast();
    }


    @Test
    public void getAllItemsApproach_2(){
        webTestClient.get().uri(ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .consumeWith( response -> {
                    List<Item> items = response.getResponseBody();
                    items.forEach( i -> {
                        assertTrue(i.getId() != null);
                    });
                });
    }

    @Test
    public void getAllItemsApproach_3(){
        Flux<Item> itemFlux =  webTestClient.get().uri(ITEM_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemFlux.log("Value from network: "))
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getOneItemTest(){
        webTestClient.get().uri(ITEM_END_POINT_V1+"/554")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.price",33.3);
    }

    @Test
    public void getOneItemTestInvalid(){
        webTestClient.get().uri(ITEM_END_POINT_V1+"/55wef4")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItemTest(){
        Item item = new Item(null, "Iphone X", 99.99);
        webTestClient.post().uri(ITEM_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone X");
    }

    @Test
    public void deleteItemTest(){
        webTestClient.delete().uri(ITEM_END_POINT_V1+"/554")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItemTest(){
        Item item = new Item(null, "Iphone X", 99.99);
        webTestClient.put().uri(ITEM_END_POINT_V1+"/554")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo( 99.99)
                .jsonPath("$.description").isEqualTo("Iphone X");
    }

    @Test
    public void updateItemTest_ID_NOT_FOUND(){
        Item item = new Item(null, "Iphone X", 99.99);
        webTestClient.put().uri(ITEM_END_POINT_V1+"/55433")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Void.class);
    }

}
