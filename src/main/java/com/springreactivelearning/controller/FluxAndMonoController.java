package com.springreactivelearning.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@PropertySource("classpath:application.properties")
public class FluxAndMonoController {


    @Value("${data.mongodb.database}")
    static  String db;

    @Autowired
    private static Environment env;
    @GetMapping("/flux")
    public Publisher<Integer> returnFlux(){

        System.out.println("DB" + db);
//        System.out.println("DB" + env.getProperty("data.mongodb.database"));
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping( value = "/flux1", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFlux2(){
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnFlux1(){
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/mono")
    public Mono<Integer> returnMono(){
        return Mono.just(1)
                .log();
    }
}
