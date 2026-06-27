package sube.interviews.mareoenvios.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sube.interviews.mareoenvios.dto.response.TopSendedResponseDto;
import sube.interviews.mareoenvios.entity.ShippingItem;

import java.util.List;

public interface ShippingItemRepository extends JpaRepository<ShippingItem, Integer> {
    @Query("SELECT new sube.interviews.mareoenvios.dto.response.TopSendedResponseDto(p.description, SUM(si.productCount)) " +
            "FROM ShippingItem si " +
            "JOIN si.product p " +
            "GROUP BY p.description " +
            "ORDER BY SUM(si.productCount) DESC")
    List<TopSendedResponseDto> findTopSendedProducts(Pageable pageable);
}
