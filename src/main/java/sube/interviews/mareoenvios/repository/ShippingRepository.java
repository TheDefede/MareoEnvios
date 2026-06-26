package sube.interviews.mareoenvios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.enums.ShippingState;

import java.time.LocalDate;

public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
    Page<Shipping> findByState(ShippingState state, Pageable pageable);
    Page<Shipping> findBySendDateBetween(LocalDate sendDateFrom, LocalDate sendDateTo, Pageable pageable);
}
