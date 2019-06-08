package com.fluxandmonoplayground;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {
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
    public void transformFluxMap(){
        Flux<String> stringFlux = Flux.fromIterable(names).log()
                .map(name -> name.toUpperCase());
        StepVerifier.create(stringFlux)
                .expectNext("BISHAL", "AKASH", "SAGAR", "SAM")
                .verifyComplete();
    }

    @Test
    public void transformFluxMap_Length(){
        Flux<Integer> stringFlux = Flux.fromIterable(names).log()
                .map(name -> name.length());
        StepVerifier.create(stringFlux)
                .expectNext(6, 5, 5, 3)
                .verifyComplete();
    }

    @Test
    public void transformFluxMap_Filter_Length_Repeat(){
        Flux<Integer> stringFlux = Flux.fromIterable(names).log()
                .map(name -> name.length())
                .filter( len -> len > 5)
                .repeat(1);
        StepVerifier.create(stringFlux)
                .expectNext(6, 6)
                .verifyComplete();
    }
}
