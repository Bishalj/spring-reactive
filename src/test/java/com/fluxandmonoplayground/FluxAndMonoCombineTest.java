package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge(){
        Flux<String> flux1 = Flux.just("A", "B", "c");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String>  mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay(){
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String>  mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay(){
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String>  concatFlux = Flux.concat(flux1, flux2);
        //takes 6 seconds
        StepVerifier.create(concatFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_withDelay_virtualTime(){
        VirtualTimeScheduler.getOrSet();
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String>  concatFlux = Flux.concat(flux1, flux2);
        //takes not even 1 seconds
        StepVerifier.withVirtualTime(() -> concatFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat(){
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String>  concatFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(concatFlux.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip(){
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String>  concatFlux = Flux.zip(flux1, flux2, (f1, f2) -> f1.concat(f2));

        StepVerifier.create(concatFlux.log())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }
}
