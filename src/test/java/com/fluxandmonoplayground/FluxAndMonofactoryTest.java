package com.fluxandmonoplayground;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonofactoryTest {

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
    public void fluxUsingIterable(){
        Flux<String> namesFlux = Flux.fromIterable(names).log();
        StepVerifier.create(namesFlux)
                .expectNext("Bishal", "Akash", "Sagar", "Sam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray(){
        String names[] = {"Bishal", "Akash", "Sagar", "Sam"};
        Flux<String> namesFlux = Flux.fromArray(names).log();
        StepVerifier.create(namesFlux)
                .expectNext("Bishal", "Akash", "Sagar", "Sam")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream(){
        Flux<String> namesFlux = Flux.fromStream(names.stream()).log();
        StepVerifier.create(namesFlux)
                .expectNext("Bishal", "Akash", "Sagar", "Sam")
                .verifyComplete();
    }
}
