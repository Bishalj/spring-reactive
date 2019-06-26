package com.springreactivelearning.controller.v1;

import com.springreactivelearning.document.ItemCapped;
import com.springreactivelearning.repository.ItemCappedReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;

import static com.springreactivelearning.constants.ItemConstants.ITEM_STREAM_END_POINT_V1;

@RestController
public class ItemStreamController {


    @Autowired
    ItemCappedReactiveRepository itemCappedReactiveRepository;

    @GetMapping(value = ITEM_STREAM_END_POINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemStream(){
        return itemCappedReactiveRepository.findItemsBy();
    }
}
