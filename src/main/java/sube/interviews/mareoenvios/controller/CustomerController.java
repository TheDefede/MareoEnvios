package sube.interviews.mareoenvios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.service.CustomerService;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operaciones de consulta de compradores")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/info/{customerId}")
    @Operation(summary = "Obtener información de un comprador", description = "Busca un comprador por su ID y lo devuelve utilizando la caché de Redis.")
    public ResponseEntity<CustomerDto> getCustomerInfo(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @GetMapping("/info")
    @Operation(summary = "Obtener listado paginado de compradores", description = "Retorna una página de compradores. Parámetros de URL opcionales: ?page=0&size=10")
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo comprador", description = "Crea un nuevo comprador en el sistema.")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.createCustomer(customerDto));
    }

    @PutMapping("/update/{customerId}")
    @Operation(summary = "Modificar un comprador existente", description = "Actualiza los datos de un comprador y invalida su caché en Redis.")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Integer customerId,
            @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDto));
    }
}