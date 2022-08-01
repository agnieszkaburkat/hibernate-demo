package com.example.hibernatedemo;

import com.example.hibernatedemo.entities.Product;
import com.example.hibernatedemo.entities.ProductCategory;
import com.example.hibernatedemo.repository.ProductCategoryRepository;
import com.example.hibernatedemo.utils.ProductDataBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveUsingCompletableFuture {

    private final PersistenceService productService;
    private final ProductCategoryRepository categoryRepository;

    @Transactional
    public void saveAllProductJdbc(StopWatch watch) {

        watch.start("JDBC Template save");
        ProductCategory categoryBatch = ProductCategory.builder().name("sci-fi jdbc").build();
        categoryRepository.saveAndFlush(categoryBatch);
        List<Product> productData = ProductDataBuilder
                .builder()
                .setTotal(1)
                .setCategory(categoryBatch)
                .buildProduct();
        log.info("Using hibernate saveAll ");
        productService.saveAll(productData);
        List<ProductCategory> productDataBatch = ProductDataBuilder
                .builder().setTotal(10).buildCategory();
        productService.saveAllCategoriesJdbcBatch(productDataBatch);

/*
        below code will ignore the transactional annotation and will be persisted no matter what:
        List<List<ProductCategory>> listOfBookSub = ListUtil.createSubList(productDataBatch, 2);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int offset = 0; offset < listOfBookSub.size(); offset++) {
            int finalOffset = offset;
            futures.add(
                    CompletableFuture.runAsync(
                            () -> {
                                productService.saveAllCategoriesJdbcBatch(listOfBookSub.get(finalOffset));
                                throw new RuntimeException("jeblo w przyszlosci");
                            }));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        log.info("Finished updating pricing configurations with new competitor.");
                */
        watch.stop();
        log.info(productDataBatch.size() + " data inserted, duration in second " + watch.prettyPrint());


    }

}
