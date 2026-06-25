package sube.interviews.mareoenvios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.ShippingItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ProductService productService;
    private final CustomerService customerService;

    public void createShipping(CreateShippingRequest request) {
        Customer customer = customerService.get(request);

        List<ShippingItem> validatedItems = productService.resolveShippingItems(request);
    }

}
