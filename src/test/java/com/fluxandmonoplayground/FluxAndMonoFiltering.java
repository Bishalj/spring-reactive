package com.fluxandmonoplayground;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFiltering {

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
    public void FilterTest(){
        Flux<String> stringFlux = Flux.fromIterable(names).log()
                .filter(name -> name.length() > 5);

        StepVerifier.create(stringFlux)
                .expectNext("Bishal")
                .verifyComplete();
    }
}
