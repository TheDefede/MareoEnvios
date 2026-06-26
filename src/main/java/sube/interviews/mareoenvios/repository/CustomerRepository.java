package sube.interviews.mareoenvios.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sube.interviews.mareoenvios.entity.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Cacheable(value = "customers", key = "#id", unless = "#result == null")
    default Optional<Customer> fetchById(Integer id) {
        return findById(id);
    }
}
