package com.example.hibernatedemo.utils;

import com.example.hibernatedemo.entities.ProductCategoryWithUUID;
import com.example.hibernatedemo.entities.ProductWithUUID;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductUuidDataBuilder {
    private final Random random = new Random();
    private ProductCategoryWithUUID category;
    private int total;

    public static ProductUuidDataBuilder builder() {
        return new ProductUuidDataBuilder();
    }


    public ProductUuidDataBuilder setCategory(ProductCategoryWithUUID productCategory) {
        category = productCategory;
        return this;
    }

    public ProductUuidDataBuilder setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<ProductWithUUID> buildProduct() {
        return IntStream.range(0, total)
                .mapToObj(val -> ProductWithUUID.builder()
                        .title("Product " + val)
                        .stock(random.nextInt(total))
                        .price((float) (random.nextInt(total) * 0.5))
                        .productCategory(category)
                        .build()
                ).collect(Collectors.toList());
    }

    public List<ProductCategoryWithUUID> buildCategory() {
        return IntStream.range(0, total)
                .mapToObj(val -> ProductCategoryWithUUID.builder()
                        .name("Category " + val)
                        .build()
                ).collect(Collectors.toList());
    }
}
