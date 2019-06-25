package com.springreactivelearning.controller.v1;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.springreactivelearning.constants.ItemConstants.ITEM_END_POINT_V1;

@RestController
public class ItemController {



    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItem(){
        return itemReactiveRepository.findAll().log();
    }

    @GetMapping("/exception")
    public Flux<Item> exception(){
        return itemReactiveRepository.findAll().concatWith(Mono.error(new RuntimeException()));
    }


    @GetMapping(ITEM_END_POINT_V1+"/{id}")
    public Mono<ResponseEntity<Item>> getAllItem1(@PathVariable("id") String id){
        return itemReactiveRepository.findById(id)
                .map( item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_END_POINT_V1)
    public Mono<ResponseEntity<Item>> createItem(@RequestBody Item item){
        return itemReactiveRepository.save(item)
                .map( items -> new ResponseEntity<>(items, HttpStatus.CREATED));
    }

    @DeleteMapping(ITEM_END_POINT_V1+"/{id}")
    public Mono<Void> deleteItem(@PathVariable("id") String id){
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping(ITEM_END_POINT_V1+"/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item){
        return itemReactiveRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemReactiveRepository.save(item);
                })
                .map(currentItem -> new ResponseEntity<>(currentItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
