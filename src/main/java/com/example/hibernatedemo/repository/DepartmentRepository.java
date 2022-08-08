package com.example.hibernatedemo.repository;

import com.example.hibernatedemo.entities.Department;
import com.example.hibernatedemo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
}
