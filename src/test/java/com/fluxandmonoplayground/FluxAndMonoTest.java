package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;

public class FluxAndMonoTest {

    @Test
    public void fluxTestWithError(){

        Flux<String> flux =  Flux.just("Spring", "Spring boot", "Spring Reactive")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("After Error")) // after the exception flux doesnt emit any data
                .log();
        flux
                .subscribe(System.out::println,
                exception -> System.out.println("Exception is : "+ exception));
    }

    @Test
    public void fluxTestWithComplete(){
        Flux<String> flux =  Flux.just("Spring", "Spring boot", "Spring Reactive")
                .log();
        flux
                .subscribe(System.out::println,
                        exception -> System.out.println(exception),
                        () -> System.out.println("Completed")
                );
    }
}
