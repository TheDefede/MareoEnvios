package sube.interviews.mareoenvios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.service.ProcessService;
import sube.interviews.mareoenvios.service.ShippingService;
import java.time.Instant;
import java.time.LocalDate;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
@Tag(name = "Shipping", description = "Operaciones de creación y gestión de envíos")
public class ShippingController {

    private final ProcessService processService;
    private final ShippingService shippingService;

    @PostMapping("/create")
    @Operation(summary = "Crear solicitud de envío", description = "Crea un nuevo envío. Recibe un customerId existente o los datos para crear un cliente nuevo, junto con la lista de productos.")
    public ResponseEntity<ShippingResponseDto> createShipping(@Valid @RequestBody CreateShippingRequest request) {
        ShippingResponseDto response = processService.process(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/info/{shippingId}")
    @Operation(summary = "Obtener información del envío", description = "Busca un envío por su ID.")
    public ResponseEntity<ShippingResponseDto> getShippingInfo(@PathVariable Integer shippingId) {
        return ResponseEntity.ok(shippingService.getShippingInfo(shippingId));
    }

    @GetMapping("/info/{sendDateFrom}/{sendDateTo}")
    @Operation(summary = "Obtener envíos por rango de fecha paginado", description = "Busca envíos paginados cuyas fechas de envío estén en el rango.")
    public ResponseEntity<Page<ShippingResponseDto>> getShippingsBySendDate(
            @PathVariable LocalDate sendDateFrom,
            @PathVariable LocalDate sendDateTo,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(shippingService.getShippingsBySendDate(sendDateFrom, sendDateTo, pageable));
    }

    @GetMapping("/info/state/{state}")
    @Operation(summary = "Obtener envíos por estado paginado", description = "Busca envíos paginados según su estado (ej: Inicial, En camino, Entregado, Cancelado).")
    public ResponseEntity<Page<ShippingResponseDto>> getShippingsByState(
            @PathVariable String state,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(shippingService.getShippingsByState(state, pageable));
    }
}
