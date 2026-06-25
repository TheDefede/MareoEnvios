package sube.interviews.mareoenvios.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
