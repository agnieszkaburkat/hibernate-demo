package com.example.hibernatedemo;

import com.example.hibernatedemo.entities.Department;
import com.example.hibernatedemo.entities.Product;
import com.example.hibernatedemo.entities.ProductCategory;
import com.example.hibernatedemo.entities.ProductWithUUID;
import com.example.hibernatedemo.repository.DepartmentRepository;
import com.example.hibernatedemo.repository.ProductCategoryRepository;
import com.example.hibernatedemo.repository.ProductRepository;
import com.example.hibernatedemo.repository.ProductWithUUIDRepository;
import com.example.hibernatedemo.utils.ListUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistenceService {

    private final DepartmentRepository departmentRepository;
    private final ProductRepository productRepository;
    private final ProductWithUUIDRepository productWithUUIDRepository;
    private final ProductCategoryRepository categoryRepository;
    private final JdbcTemplate template;
    private final HikariDataSource hikariDataSource;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional
    public void saveAll(List<Product> productData) {
        log.info("insert using saveAll");
        productRepository.saveAll(productData);
    }

    @Transactional
    public void saveAllDepartment(Department department) {
        log.info("insert using saveAll");
        departmentRepository.save(department);
    }

    @Transactional
    public void saveAllUuid(List<ProductWithUUID> productDataWithUUID) {
        log.info("insert entities with uuid using saveAll");
        productWithUUIDRepository.saveAll(productDataWithUUID);
    }

    @Transactional
    public void saveAllJdbcBatch(List<Product> productData) {
        log.info("insert using jdbc batch");
        String sql = String.format(
                "INSERT INTO %s (title, stock, price, product_category_id) " +
                        "VALUES (?, ?, ?, ?)",
                Product.class.getAnnotation(Table.class).name()
        );
        template.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, productData.get(i).getTitle());
                        ps.setInt(2, productData.get(i).getStock());
                        ps.setFloat(3, productData.get(i).getPrice());
                        ps.setLong(4, productData.get(i).getProductCategory().getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return productData.size();
                    }
                });

    }

    @Transactional
    public void saveAllJdbcBatchWithExecutorService(List<Product> productData) {
        int maximumPoolSize = hikariDataSource.getMaximumPoolSize();
        log.info("pool size: {}", maximumPoolSize);
        ExecutorService executorService = Executors.newFixedThreadPool(maximumPoolSize);
        List<List<Product>> listOfBookSub = ListUtil.createSubList(productData, batchSize);
        List<Callable<Void>> callables = listOfBookSub.stream().map(sublist ->
                (Callable<Void>) () -> {
                    saveAllJdbcBatch(sublist);
                    return null;
                }).collect(Collectors.toList());
        try {
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCategory(ProductCategory category) {
        categoryRepository.save(category);
    }

    public void saveAllCategoriesJdbcBatch(List<ProductCategory> productData) {
        log.info("insert using jdbc batch");
        String sql = String.format(
                "INSERT INTO %s (name) " +
                        "VALUES (?)",
                ProductCategory.class.getAnnotation(Table.class).name()
        );
        template.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, productData.get(i).getName());
                    }

                    @Override
                    public int getBatchSize() {
                        return productData.size();
                    }
                });
    }
    public long countAllCategories() {
        return categoryRepository.count();
    }

    public long countAllProducts() {
        return productRepository.count();
    }

}
