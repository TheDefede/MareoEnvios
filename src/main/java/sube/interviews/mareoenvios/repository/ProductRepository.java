package sube.interviews.mareoenvios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
