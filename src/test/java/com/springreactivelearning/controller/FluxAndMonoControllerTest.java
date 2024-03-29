package com.springreactivelearning.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class FluxAndMonoControllerTest {

        @Autowired
        WebTestClient webTestClient;

        @Test
        public void flux_approach1() {
            Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .returnResult(Integer.class)
                    .getResponseBody();

            StepVerifier.create(integerFlux)
                    .expectSubscription()
                    .expectNext(1)
                    .expectNext(2)
                    .expectNext(3)
                    .expectNext(4)
                    .verifyComplete();
        }
    @Test
    public void flux_approach2(){

            webTestClient.get().uri("/flux")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .expectBodyList(Integer.class)
                    .hasSize(4);
        }

    @Test
    public void flux_approach3(){

        List<Integer> expectedList = Arrays.asList(1,2,3,4);
        EntityExchangeResult<List<Integer>> listResult = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedList, listResult.getResponseBody());
    }

    @Test
    public void flux_approach4(){
        List<Integer> expectedList = Arrays.asList(1,2,3,4);

        webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .consumeWith((response) ->
                        assertEquals(expectedList, response.getResponseBody()));
    }

    @Test
    public void fluxStream(){
            Flux<Long> longStreamFlux = webTestClient.get().uri("/fluxstream")
                    .accept(MediaType.APPLICATION_STREAM_JSON)
                    .exchange()
                    .expectStatus().isOk()
//                    .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .returnResult(Long.class)
                    .getResponseBody();


        StepVerifier.create(longStreamFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();
    }

    @Test
    public void MonoStream(){
       Integer value = new Integer(1);
       webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith( response -> {
                    assertEquals(value, response.getResponseBody());
                });
    }
}
