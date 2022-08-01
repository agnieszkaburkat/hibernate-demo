package com.example.hibernatedemo.repository;

import com.example.hibernatedemo.entities.ProductWithUUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductWithUUIDRepository extends JpaRepository<ProductWithUUID, Long> {
}
