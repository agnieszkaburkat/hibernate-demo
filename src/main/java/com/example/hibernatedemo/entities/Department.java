package com.example.hibernatedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Table(name = "department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "department",
            targetEntity = ProductCategoryWithUUID.class,
            fetch = FetchType.LAZY)
    private List<ProductCategoryWithUUID> productCategories;
}
