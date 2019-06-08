package com.fluxandmonoplayground;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoFlatMapTest {

    List<String> names;

    @Before
    public void inititalizeData(){
        this.names = Arrays.asList(
                "Bishal",
                "Akash",
                "Sagar",
                "Sam"
        );
    }

    @Test
    public void Flux_FlatmapTest(){
        Flux<String> stringFlux = Flux.fromIterable(names)
                // flatMap is used when a source return a flux like db calls or BE to BE calls
                .flatMap( name -> {
                    return Flux.fromIterable(convertToList(name));
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(8)
                .verifyComplete();
    }

    private List<String> convertToList(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(name, "new");
    }

    @Test
    public void Flux_Flatmap_using_parallelTest(){
        Flux<String> stringFlux = Flux.fromIterable(names)
                .window(2)
                .flatMap( (name) ->
                     name.map(this::convertToList).subscribeOn(parallel())) //Flux<List<String>>
                .flatMap(name -> Flux.fromIterable(name)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void Flux_Flatmap_using_parallel_maintain_order(){
        Flux<String> stringFlux = Flux.fromIterable(names)
                .window(2)
                .flatMapSequential( (name) ->
                        name.map(this::convertToList).subscribeOn(parallel())) //Flux<List<String>>
                .flatMap(name -> Flux.fromIterable(name)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(8)
                .verifyComplete();
    }
}
