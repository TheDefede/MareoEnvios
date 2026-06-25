package sube.interviews.mareoenvios.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sube.interviews.mareoenvios.enums.ShippingState;
import sube.interviews.mareoenvios.enums.ShippingType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private ShippingState state;

    @Column(name = "send_date")
    private LocalDate sendDate;

    @Column(name = "arrive_date")
    private LocalDate arriveDate;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    private ShippingType type;

    @OneToMany(mappedBy = "shipping", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShippingItem> items = new ArrayList<>();
    public void addItem(ShippingItem item) {
        items.add(item);
        item.setShipping(this);
    }
}
