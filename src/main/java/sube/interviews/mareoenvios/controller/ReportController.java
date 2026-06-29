package sube.interviews.mareoenvios.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sube.interviews.mareoenvios.dto.response.TopSendedResponseDto;
import sube.interviews.mareoenvios.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Reportes y estadísticas")
public class ReportController {

    private final ProductService productService;

    @GetMapping("/topSended")
    @Operation(summary = "Obtener los 3 productos más solicitados", description = "Retorna el listado de los 3 productos más enviados con su descripción y cantidad total.")
    @Parameter(description = "Cantidad máxima de productos a incluir en el top", example = "3")
    public ResponseEntity<List<TopSendedResponseDto>> getTopSendedProducts(@RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(productService.getTopSendedProducts(limit));
    }
}
