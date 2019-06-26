package com.springreactivelearning.initialize;

import com.springreactivelearning.document.Item;
import com.springreactivelearning.document.ItemCapped;
import com.springreactivelearning.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void run(String... args) throws Exception {
        initializeDataSetup();
        createItemCapped();

    }

    private void createItemCapped() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(5000).capped());
    }

    public List<Item> data(){
        return Arrays.asList(
                new Item("55", "444", 33.3),
                new Item("56", "444", 33.3),
                new Item(null, "444", 33.3)
        );
    }


    private void initializeDataSetup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data())
                    .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll()))
                .subscribe(
                        item -> System.out.println(" inserted from command line runner: "+item)
                );
    }
}
