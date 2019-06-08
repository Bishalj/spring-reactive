package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void Flux_Without_Virtual_Time(){

        Flux<Long> longFlux =  Flux.interval(Duration.ofSeconds(1))
                .take(4)
                .log();
        //this process takes 3 seconds and increases the build time
        StepVerifier.create(longFlux)
                .expectSubscription()
                .expectNext(0l, 1l, 2l, 3l)
                .verifyComplete();
    }

    @Test
    public void Flux_With_Virtual_Time(){
        VirtualTimeScheduler.getOrSet();

        Flux<Long> longFlux =  Flux.interval(Duration.ofSeconds(1))
                .take(4)
                .log();
        //this process takes not even 1 seconds and decreases the build time by lot
        StepVerifier.withVirtualTime(() -> longFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(4))
                .expectNext(0l, 1l, 2l, 3l)
                .verifyComplete();
    }
}
