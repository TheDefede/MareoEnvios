package sube.interviews.mareoenvios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.service.ShippingService;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
@Tag(name = "Shipping", description = "Operaciones de creación y gestión de envíos")
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/create")
    @Operation(summary = "Crear solicitud de envío", description = "Crea un nuevo envío. Recibe un customerId existente o los datos para crear un cliente nuevo, junto con la lista de productos.")
    public ResponseEntity<Shipping> createShipping(@Valid @RequestBody CreateShippingRequest request) {
        shippingService.createShipping(request);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
