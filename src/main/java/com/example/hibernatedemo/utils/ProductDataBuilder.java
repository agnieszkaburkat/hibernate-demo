package com.example.hibernatedemo.utils;

import com.example.hibernatedemo.entities.Product;
import com.example.hibernatedemo.entities.ProductCategory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductDataBuilder {
    private final Random random = new Random();
    private ProductCategory category;
    private int total;

    public static ProductDataBuilder builder(){
        return new ProductDataBuilder();
    }


    public ProductDataBuilder setCategory(ProductCategory productCategory){
        category = productCategory;
        return this;
    }

    public ProductDataBuilder setTotal(int total){
        this.total = total;
        return this;
    }

    public List<Product> buildProduct(){
        return IntStream.range(0, total)
                .mapToObj(val -> Product.builder()
                        .title("Product " + val)
                        .stock(random.nextInt(total))
                        .price((float) (random.nextInt(total) * 0.5))
                        .productCategory(category)
                        .build()
                ).collect(Collectors.toList());
    }

    public List<ProductCategory> buildCategory(){
        return IntStream.range(0, total)
                .mapToObj(val -> ProductCategory.builder()
                        .name("Product " + val)
                        .build()
                ).collect(Collectors.toList());
    }
}
