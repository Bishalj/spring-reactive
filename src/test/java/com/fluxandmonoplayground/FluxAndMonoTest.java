package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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

    @Test
    public void fluxTestElementsWithoutError(){
        Flux<String> flux =  Flux.just("Spring", "Spring boot", "Spring Reactive")
                .log();
        StepVerifier.create(flux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Spring Reactive")
                .verifyComplete();
    }

    @Test
    public void fluxTestElementsWithErrorMessage(){
        Flux<String> flux =  Flux.just("Spring", "Spring boot", "Spring Reactive")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .log();
        StepVerifier.create(flux)
                .expectNext("Spring", "Spring boot", "Spring Reactive")
                .expectErrorMessage("Exception Occured")
                .verify();
    }

    @Test
    public void fluxTestElementsWithError(){
        Flux<String> flux =  Flux.just("Spring", "Spring boot", "Spring Reactive")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .log();
        StepVerifier.create(flux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Spring Reactive")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestElementsCountWithError(){
        Flux<String> flux =  Flux.just("Spring", "Spring boot", "Spring Reactive")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .log();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                .verify();
    }

}
