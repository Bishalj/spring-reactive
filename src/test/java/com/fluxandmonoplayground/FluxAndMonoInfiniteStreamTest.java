package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.Flushable;
import java.time.Duration;

public class FluxAndMonoInfiniteStreamTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
        Flux<Long> infiniteStream = Flux.interval(Duration.ofMillis(100)).log();

        infiniteStream.subscribe((element) -> System.out.println("The value is: "+element));
        Thread.sleep(3000);
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> infiniteStream = Flux.interval(Duration.ofMillis(100))
                .take(3)  //restrict the stream and takes only 2 elements
                .log();

        StepVerifier.create(infiniteStream)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceTestMap() throws InterruptedException {
        Flux<Integer> infiniteStream = Flux.interval(Duration.ofMillis(100))
                .map(s -> new Integer(s.intValue()))
                .take(3)  //restrict the stream and takes only 2 elements
                .log();

        StepVerifier.create(infiniteStream)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}
