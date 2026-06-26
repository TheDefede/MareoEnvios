package sube.interviews.mareoenvios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sube.interviews.mareoenvios.dto.response.CustomerResponseDto;
import sube.interviews.mareoenvios.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operaciones de consulta de compradores")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/info/{customerId}")
    @Operation(summary = "Obtener información de un comprador", description = "Busca un comprador por su ID y lo devuelve utilizando la caché de Redis.")
    public ResponseEntity<CustomerResponseDto> getCustomerInfo(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @GetMapping("/info")
    @Operation(summary = "Obtener listado paginado de compradores", description = "Retorna una página de compradores. Parámetros de URL opcionales: ?page=0&size=10")
    public ResponseEntity<Page<CustomerResponseDto>> getAllCustomers(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }
}