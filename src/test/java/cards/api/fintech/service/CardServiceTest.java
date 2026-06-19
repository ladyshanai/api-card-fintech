package cards.api.fintech.service;

import cards.api.fintech.client.AccountClient;
import cards.api.fintech.client.AccountModel;
import cards.api.fintech.dto.CardRequest;
import cards.api.fintech.dto.CardResponse;
import cards.api.fintech.entity.CardEntity;
import cards.api.fintech.enums.CardBrand;
import cards.api.fintech.enums.CardStatus;
import cards.api.fintech.enums.CardType;
import cards.api.fintech.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardService Tests")
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private CardService cardService;

    private CardEntity cardEntity;
    private CardRequest cardRequest;
    private AccountModel accountModel;

    @BeforeEach
    public void setUp() {
        // Configurar datos de prueba
        accountModel = new AccountModel(
                1L,
                1L,
                "John",
                "ACC123456",
                "USD",
                new BigDecimal("10000.00"),
                new BigDecimal("50000.00"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        cardEntity = new CardEntity();
        cardEntity.setId(1L);
        cardEntity.setAccountId(1L);
        cardEntity.setCardNumber("450912345678");
        cardEntity.setCardType(CardType.CREDIT);
        cardEntity.setCardBrand(CardBrand.VISA);
        cardEntity.setCardStatus(CardStatus.ACTIVE);
        cardEntity.setCreditLimit(new BigDecimal("5000.00"));
        cardEntity.setAvailableBalance(new BigDecimal("5000.00"));
        cardEntity.setUsedBalance(new BigDecimal("0.00"));
        cardEntity.setExpirationDate(LocalDate.of(2026, 12, 31));
        cardEntity.setIssueDate(LocalDate.of(2023, 1, 1));
        cardEntity.setCreatedAt(LocalDateTime.now());
        cardEntity.setUpdatedAt(LocalDateTime.now());

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
    @DisplayName("Should create card successfully")
    void testCreateCardSuccess() {
        // Arrange
        when(accountClient.getAccountById(1L)).thenReturn(accountModel);
        when(cardRepository.save(any(CardEntity.class))).thenReturn(cardEntity);

        // Act
        CardResponse response = cardService.createCard(cardRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.accountId());
        assertEquals(CardType.CREDIT, response.cardType());
        assertEquals(CardBrand.VISA, response.cardBrand());
        assertEquals(CardStatus.ACTIVE, response.cardStatus());
        assertEquals(new BigDecimal("5000.00"), response.creditLimit());

        verify(accountClient, times(1)).getAccountById(1L);
        verify(cardRepository, times(1)).save(any(CardEntity.class));
    }

    @Test
    @DisplayName("Should get all cards")
    void testGetAllCards() {
        // Arrange
        List<CardEntity> cardList = List.of(cardEntity);
        when(cardRepository.findAll()).thenReturn(cardList);

        // Act
        List<CardResponse> responses = cardService.getCards();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals(CardType.CREDIT, responses.get(0).cardType());

        verify(cardRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get card by id successfully")
    void testGetCardByIdSuccess() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(cardEntity));

        // Act
        CardResponse response = cardService.getCardById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("450912345678", response.cardNumber());

        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when card not found by id")
    void testGetCardByIdNotFound() {
        // Arrange
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cardService.getCardById(999L));
        verify(cardRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get cards by account id")
    void testGetCardsByAccountId() {
        // Arrange
        List<CardEntity> cardList = List.of(cardEntity);
        when(cardRepository.findByAccountId(1L)).thenReturn(cardList);

        // Act
        List<CardResponse> responses = cardService.getCardsByAccount(1L);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).accountId());

        verify(cardRepository, times(1)).findByAccountId(1L);
    }

    @Test
    @DisplayName("Should get cards by status")
    void testGetCardsByStatus() {
        // Arrange
        List<CardEntity> cardList = List.of(cardEntity);
        when(cardRepository.findByCardStatus(CardStatus.ACTIVE)).thenReturn(cardList);

        // Act
        List<CardResponse> responses = cardService.getCardsByStatus(CardStatus.ACTIVE);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(CardStatus.ACTIVE, responses.get(0).cardStatus());

        verify(cardRepository, times(1)).findByCardStatus(CardStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should update card status successfully")
    void testUpdateCardStatusSuccess() {
        // Arrange
        CardEntity updatedCard = new CardEntity();
        updatedCard.setId(1L);
        updatedCard.setAccountId(1L);
        updatedCard.setCardNumber("450912345678");
        updatedCard.setCardType(CardType.CREDIT);
        updatedCard.setCardBrand(CardBrand.VISA);
        updatedCard.setCardStatus(CardStatus.BLOCKED);
        updatedCard.setCreditLimit(new BigDecimal("5000.00"));
        updatedCard.setAvailableBalance(new BigDecimal("5000.00"));
        updatedCard.setUsedBalance(new BigDecimal("0.00"));
        updatedCard.setExpirationDate(LocalDate.of(2026, 12, 31));
        updatedCard.setIssueDate(LocalDate.of(2023, 1, 1));
        updatedCard.setCreatedAt(LocalDateTime.now());
        updatedCard.setUpdatedAt(LocalDateTime.now());

        when(cardRepository.findById(1L)).thenReturn(Optional.of(cardEntity));
        when(cardRepository.save(any(CardEntity.class))).thenReturn(updatedCard);

        // Act
        CardResponse response = cardService.updateStatus(1L, CardStatus.BLOCKED);

        // Assert
        assertNotNull(response);
        assertEquals(CardStatus.BLOCKED, response.cardStatus());

        verify(cardRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).save(any(CardEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when updating status of non-existent card")
    void testUpdateCardStatusNotFound() {
        // Arrange
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cardService.updateStatus(999L, CardStatus.BLOCKED));
        verify(cardRepository, times(1)).findById(999L);
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete card successfully")
    void testDeleteCardSuccess() {
        // Arrange
        when(cardRepository.findById(1L)).thenReturn(Optional.of(cardEntity));

        // Act
        cardService.deleteById(1L);

        // Assert
        verify(cardRepository, times(1)).findById(1L);
        verify(cardRepository, times(1)).delete(cardEntity);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent card")
    void testDeleteCardNotFound() {
        // Arrange
        when(cardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cardService.deleteById(999L));
        verify(cardRepository, times(1)).findById(999L);
        verify(cardRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should return empty list when no cards found")
    void testGetCardsEmptyList() {
        // Arrange
        when(cardRepository.findAll()).thenReturn(List.of());

        // Act
        List<CardResponse> responses = cardService.getCards();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(cardRepository, times(1)).findAll();
    }
}


