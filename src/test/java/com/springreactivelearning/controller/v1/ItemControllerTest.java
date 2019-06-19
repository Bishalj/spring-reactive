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

}
