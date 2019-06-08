package com.fluxandmonoplayground;

import org.junit.Test;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .thenRequest(2)
                .expectNext(1, 2)
                .thenRequest(2)
                .expectNext(3, 4)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        finiteFlux.subscribe(
                (element) -> System.out.println("The element is: "+ element),
                (excep) -> System.out.println("The exception is : "+excep.getMessage()),
                () -> System.out.println("Complete"),
                (subscribtion -> subscribtion.request(2))
        );
    }

    @Test
    public void backPressure_cancel(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        finiteFlux.subscribe(
                (element) -> System.out.println("The element is: "+ element),
                (excep) -> System.out.println("The exception is : "+excep.getMessage()),
                () -> System.out.println("Complete"),
                (subscribtion -> subscribtion.cancel())
        );
    }

    @Test
    public void backPressure_customized(){
        Flux<Integer> finiteFlux = Flux.range(1, 10).log();

        finiteFlux.subscribe(
                new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        request(1);
                        System.out.println("The value is: "+ value);
                        if(value == 7)
                            cancel();
                    }
                }
        );
    }
}
