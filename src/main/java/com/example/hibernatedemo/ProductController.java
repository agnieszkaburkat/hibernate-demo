package com.example.hibernatedemo;

import com.example.hibernatedemo.entities.Product;
import com.example.hibernatedemo.entities.ProductCategory;
import com.example.hibernatedemo.entities.ProductCategoryWithUUID;
import com.example.hibernatedemo.entities.ProductWithUUID;
import com.example.hibernatedemo.repository.ProductCategoryRepository;
import com.example.hibernatedemo.utils.ProductDataBuilder;
import com.example.hibernatedemo.utils.ProductUuidDataBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {

    private final SaveUsingCompletableFuture saveUsingCompletableFuture;
    private final PersistenceService persistenceService;

    private static final int ENTITIES_SIZE = 10_000;

    @GetMapping(path = "/exception")
    public void saveAllProductsWithExceptionThrown() {
        tryToPersistData();
        long categoriesSize = persistenceService.countAllCategories();
        assert categoriesSize == 0L;
        long productsSize = persistenceService.countAllProducts();
        assert productsSize == 0L;
    }

    private void tryToPersistData() {
        StopWatch watch = new StopWatch();

        watch.start("Hibernate saveAll()");
        ProductCategory category = ProductCategory.builder().name("sci-fi save all").build();
        List<Product> productData = ProductDataBuilder
                .builder()
                .setTotal(ENTITIES_SIZE)
                .setCategory(category)
                .buildProduct();
        log.info("Using hibernate saveAll ");
        persistenceService.saveAll(productData);
        watch.stop();

        saveUsingCompletableFuture.saveAllProductJdbc(watch);

        log.info(productData.size() + " data inserted, duration in second " + watch.prettyPrint());
        throw new RuntimeException("There was an exception thrown, nothing should be persisted");
    }

    @GetMapping(path = "/compare")
    public void saveAllProductsComparingHibernateAndJdbc() {
        StopWatch watch = new StopWatch();

        watch.start("Hibernate saveAll()");
        ProductCategory category = ProductCategory.builder().name("sci-fi save all").build();
        List<Product> productData = ProductDataBuilder
                .builder()
                .setTotal(ENTITIES_SIZE)
                .setCategory(category)
                .buildProduct();
        log.info("Using hibernate saveAll ");
        persistenceService.saveAll(productData);
        watch.stop();

        watch.start("UUID Hibernate saveAll()");
        ProductCategoryWithUUID categoryWithUUID = ProductCategoryWithUUID.builder().name("sci-fi save all uuid").build();
        List<ProductWithUUID> productDataWithUUID = ProductUuidDataBuilder
                .builder()
                .setTotal(ENTITIES_SIZE)
                .setCategory(categoryWithUUID)
                .buildProduct();
        log.info("Using hibernate saveAll entities with UUID ");
        persistenceService.saveAllUuid(productDataWithUUID);
        watch.stop();

        watch.start("JDBC Template save with Executor Service");
        ProductCategory categoryBatchForMultithread = ProductCategory.builder().name("sci-fi jdbc multithread").build();
        persistenceService.saveCategory(categoryBatchForMultithread);
        persistenceService.saveAllJdbcBatchWithExecutorService(ProductDataBuilder
                .builder()
                .setTotal(ENTITIES_SIZE)
                .setCategory(categoryBatchForMultithread)
                .buildProduct());
        watch.stop();

        watch.start("JDBC Template save");
        ProductCategory categoryBatch = ProductCategory.builder().name("sci-fi jdbc").build();
        persistenceService.saveCategory(categoryBatch);
        List<Product> productDataBatch = ProductDataBuilder
                .builder()
                .setTotal(ENTITIES_SIZE)
                .setCategory(categoryBatch)
                .buildProduct();
        persistenceService.saveAllJdbcBatch(productDataBatch);
        watch.stop();

        log.info(productData.size() + " data inserted, duration in second " + watch.prettyPrint());

    }


}
