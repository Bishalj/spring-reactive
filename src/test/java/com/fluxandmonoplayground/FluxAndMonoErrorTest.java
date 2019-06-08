package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling(){
        Flux<String> fluxString = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorResume( e -> {
                    System.out.println(e.getMessage());
                    //need to return flux
                    return Flux.just("Default", "Default1");
                }).log();

        StepVerifier.create(fluxString)
                .expectNext("A", "B", "C")
                .expectNext("Default", "Default1")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn(){
        Flux<String> fluxString = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorReturn(
                    //need to return only value
                     "Default"
                ).log();

        StepVerifier.create(fluxString)
                .expectNext("A", "B", "C")
                .expectNext("Default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap(){
        Flux<String> fluxString = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap( (e) -> {
                    return new CustomException(e.getMessage());
                }).log();

        StepVerifier.create(fluxString)
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_Retry(){
        Flux<String> fluxString = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap( (e) -> {
                    return new CustomException(e.getMessage());
                })
                .retry(2)
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }
    @Test
    public void fluxErrorHandling_onErrorMap_RetryBackOff(){
        Flux<String> fluxString = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occured")))
                .concatWith(Flux.just("D"))
                .onErrorMap( (e) -> {
                    return new CustomException(e.getMessage());
                })
                .retryBackoff(2, Duration.ofSeconds(5))
                .log();

        StepVerifier.create(fluxString)
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

}
