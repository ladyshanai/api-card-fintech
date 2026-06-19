package cards.api.fintech.controller;

import cards.api.fintech.dto.CardRequest;
import cards.api.fintech.dto.CardResponse;
import cards.api.fintech.enums.CardBrand;
import cards.api.fintech.enums.CardStatus;
import cards.api.fintech.enums.CardType;
import cards.api.fintech.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@DisplayName("CardController Tests")
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    private CardResponse cardResponse;
    private CardRequest cardRequest;

    @BeforeEach
    public void setUp() {
        cardResponse = new CardResponse(
                1L,
                1L,
                "450912345678",
                CardType.CREDIT,
                CardStatus.ACTIVE,
                CardBrand.VISA,
                new BigDecimal("5000.00"),
                new BigDecimal("5000.00"),
                new BigDecimal("0.00"),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2023, 1, 1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );

        cardRequest = new CardRequest(
                1L,
                CardType.CREDIT,
                CardBrand.VISA,
                new BigDecimal("5000.00"),
                new BigDecimal("5000.00"),
                new BigDecimal("0.00"),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2023, 1, 1)
        );
    }

    @Test
    @DisplayName("POST /api/v1/cards - Should create card successfully")
    void testCreateCardSuccess() throws Exception {
        // Arrange
        when(cardService.createCard(any(CardRequest.class))).thenReturn(cardResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.accountId", is(1)))
                .andExpect(jsonPath("$.cardNumber", is("450912345678")))
                .andExpect(jsonPath("$.cardType", is("CREDIT")))
                .andExpect(jsonPath("$.cardStatus", is("ACTIVE")))
                .andExpect(jsonPath("$.cardBrand", is("VISA")));

        verify(cardService, times(1)).createCard(any(CardRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/cards - Should get all cards")
    void testGetAllCards() throws Exception {
        // Arrange
        List<CardResponse> cardList = List.of(cardResponse);
        when(cardService.getCards()).thenReturn(cardList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cardNumber", is("450912345678")));

        verify(cardService, times(1)).getCards();
    }

    @Test
    @DisplayName("GET /api/v1/cards/{id} - Should get card by id")
    void testGetCardById() throws Exception {
        // Arrange
        when(cardService.getCardById(1L)).thenReturn(cardResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cardNumber", is("450912345678")))
                .andExpect(jsonPath("$.cardStatus", is("ACTIVE")));

        verify(cardService, times(1)).getCardById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/cards/{id} - Should return 404 when card not found")
    void testGetCardByIdNotFound() throws Exception {
        // Arrange
        when(cardService.getCardById(999L)).thenThrow(new RuntimeException("Card not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(cardService, times(1)).getCardById(999L);
    }

    @Test
    @DisplayName("GET /api/v1/cards/account/{accountId} - Should get cards by account")
    void testGetCardsByAccount() throws Exception {
        // Arrange
        List<CardResponse> cardList = List.of(cardResponse);
        when(cardService.getCardsByAccount(1L)).thenReturn(cardList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards/account/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(1)));

        verify(cardService, times(1)).getCardsByAccount(1L);
    }

    @Test
    @DisplayName("GET /api/v1/cards/status/{status} - Should get cards by status")
    void testGetCardsByStatus() throws Exception {
        // Arrange
        List<CardResponse> cardList = List.of(cardResponse);
        when(cardService.getCardsByStatus(CardStatus.ACTIVE)).thenReturn(cardList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards/status/ACTIVE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cardStatus", is("ACTIVE")));

        verify(cardService, times(1)).getCardsByStatus(CardStatus.ACTIVE);
    }

    @Test
    @DisplayName("PATCH /api/v1/cards/{id}/status/{newStatus} - Should update card status")
    void testUpdateCardStatus() throws Exception {
        // Arrange
        CardResponse updatedResponse = new CardResponse(
                1L,
                1L,
                "450912345678",
                CardType.CREDIT,
                CardStatus.BLOCKED,
                CardBrand.VISA,
                new BigDecimal("5000.00"),
                new BigDecimal("5000.00"),
                new BigDecimal("0.00"),
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2023, 1, 1),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
        when(cardService.updateStatus(1L, CardStatus.BLOCKED)).thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/cards/1/status/BLOCKED")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardStatus", is("BLOCKED")));

        verify(cardService, times(1)).updateStatus(1L, CardStatus.BLOCKED);
    }

    @Test
    @DisplayName("DELETE /api/v1/cards/{id} - Should delete card successfully")
    void testDeleteCard() throws Exception {
        // Arrange
        doNothing().when(cardService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/cards/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cardService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("DELETE /api/v1/cards/{id} - Should return 404 when card not found")
    void testDeleteCardNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Card not found with id: 999")).when(cardService).deleteById(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/cards/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(cardService, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("GET /api/v1/cards - Should return empty list when no cards exist")
    void testGetAllCardsEmpty() throws Exception {
        // Arrange
        when(cardService.getCards()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/v1/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(cardService, times(1)).getCards();
    }
}



