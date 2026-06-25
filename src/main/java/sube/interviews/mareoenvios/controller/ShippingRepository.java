package sube.interviews.mareoenvios.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.Shipping;

import java.time.LocalDate;
import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
    List<Shipping> findByState(String state);
    List<Shipping> findBySendDateBetween(LocalDate sendDateFrom, LocalDate sendDateTo);
}
