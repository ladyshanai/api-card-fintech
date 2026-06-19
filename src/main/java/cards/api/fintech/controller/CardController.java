package cards.api.fintech.controller;

import cards.api.fintech.dto.CardRequest;
import cards.api.fintech.dto.CardResponse;
import cards.api.fintech.enums.CardStatus;
import cards.api.fintech.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@Tag(name = "Cards", description = "API para gestionar tarjetas de crédito y débito")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva tarjeta", description = "Crea una nueva tarjeta asociada a una cuenta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarjeta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest) {
        var response = cardService.createCard(cardRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tarjetas", description = "Obtiene lista de todas las tarjetas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de tarjetas")
    public ResponseEntity<List<CardResponse>> getCards() {
        var response = cardService.getCards();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tarjeta por ID", description = "Obtiene detalles de una tarjeta específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarjeta encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    })
    public ResponseEntity<CardResponse> getCardById(@Parameter(description = "ID de tarjeta") @PathVariable Long id) {
        var response = cardService.getCardById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Obtener tarjetas por cuenta", description = "Obtiene las tarjetas asociadas a una cuenta")
    @ApiResponse(responseCode = "200", description = "Lista de tarjetas de la cuenta")
    public ResponseEntity<List<CardResponse>> getCardsByAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long accountId) {
        var response = cardService.getCardsByAccount(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtener tarjetas por estado", description = "Obtiene tarjetas filtradas por estado")
    @ApiResponse(responseCode = "200", description = "Lista de tarjetas por estado")
    public ResponseEntity<List<CardResponse>> getCardsByStatus(
            @Parameter(description = "Estado de la tarjeta") @PathVariable CardStatus status) {
        var response = cardService.getCardsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status/{newStatus}")
    @Operation(summary = "Actualizar estado de tarjeta", description = "Actualiza el estado de una tarjeta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    })
    public ResponseEntity<CardResponse> updateStatus(
            @Parameter(description = "ID de la tarjeta") @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la tarjeta") @PathVariable CardStatus newStatus
    ) {
        var response = cardService.updateStatus(id, newStatus);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tarjeta", description = "Elimina una tarjeta por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarjeta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarjeta no encontrada")
    })
    public ResponseEntity<Void> deleteCard(
            @Parameter(description = "ID de la tarjeta a eliminar") @PathVariable Long id) {
        cardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}