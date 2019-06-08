package com.fluxandmonoplayground;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

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

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> mono = Mono.justOrEmpty(null); // Mono.Empty()
        StepVerifier.create(mono.log())  // not going to emit any data as there is no data
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Supplier<String> stringSupplier = () -> "Bishal";

        System.out.println(stringSupplier.get());

        Mono<String> mono = Mono.fromSupplier(stringSupplier);
        StepVerifier.create(mono.log())
                .expectNext("Bishal")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){

        Flux<Integer> integerFlux = Flux.range(0,6).log();
        StepVerifier.create(integerFlux)
                .expectNext(0, 1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
