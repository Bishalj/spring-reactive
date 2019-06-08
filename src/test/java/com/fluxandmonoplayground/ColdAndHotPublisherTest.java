package com.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.io.Flushable;
import java.time.Duration;

public class ColdAndHotPublisherTest {

    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofSeconds(1))
                .log();


        stringFlux.subscribe(e -> System.out.println("F1: "+ e));
        Thread.sleep(1000);


        stringFlux.subscribe(e -> System.out.println("F2: "+ e));
        Thread.sleep(4000);

    }

    @Test
    public void hotPublisher() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F", "G", "H")
                .delayElements(Duration.ofSeconds(1))
                .log();
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(e -> System.out.println("F1: "+ e));
        Thread.sleep(3000);


        connectableFlux.subscribe(e -> System.out.println("F2: "+ e));
        Thread.sleep(7000);

    }
}
