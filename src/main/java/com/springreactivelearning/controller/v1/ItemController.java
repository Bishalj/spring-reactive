package com.springreactivelearning.controller.v1;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.springreactivelearning.constants.ItemConstants.ITEM_END_POINT_V1;

@RestController
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItem(){
        return itemReactiveRepository.findAll()
                ;
    }
}
