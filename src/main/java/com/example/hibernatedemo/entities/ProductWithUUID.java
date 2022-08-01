package com.example.hibernatedemo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.type.UUIDCharType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.UUID;

@Builder
@Entity
@Table(name = "product_uuid")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithUUID {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private Integer stock;
    private Float price;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    @Cascade(CascadeType.ALL)
    private ProductCategoryWithUUID productCategory;
}
