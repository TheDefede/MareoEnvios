package sube.interviews.mareoenvios.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import sube.interviews.mareoenvios.entity.ShippingItem;

public interface ShippingItemRepository extends JpaRepository<ShippingItem, Integer> {
}
