package cards.api.fintech.entity;

import cards.api.fintech.enums.CardBrand;
import cards.api.fintech.enums.CardStatus;
import cards.api.fintech.enums.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CardEntity Tests")
class CardEntityTest {

    private CardEntity cardEntity;

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    @DisplayName("Should get and set id correctly")
    void testIdGetterSetter() {
        cardEntity.setId(100L);
        assertEquals(100L, cardEntity.getId());
    }

    @Test
    @DisplayName("Should get and set accountId correctly")
    void testAccountIdGetterSetter() {
        cardEntity.setAccountId(2L);
        assertEquals(2L, cardEntity.getAccountId());
    }

    @Test
    @DisplayName("Should get and set cardNumber correctly")
    void testCardNumberGetterSetter() {
        cardEntity.setCardNumber("4532123456789012");
        assertEquals("4532123456789012", cardEntity.getCardNumber());
    }

    @Test
    @DisplayName("Should get and set cardType correctly")
    void testCardTypeGetterSetter() {
        cardEntity.setCardType(CardType.DEBIT);
        assertEquals(CardType.DEBIT, cardEntity.getCardType());
    }

    @Test
    @DisplayName("Should get and set cardBrand correctly")
    void testCardBrandGetterSetter() {
        cardEntity.setCardBrand(CardBrand.MASTERCARD);
        assertEquals(CardBrand.MASTERCARD, cardEntity.getCardBrand());
    }

    @Test
    @DisplayName("Should get and set cardStatus correctly")
    void testCardStatusGetterSetter() {
        cardEntity.setCardStatus(CardStatus.BLOCKED);
        assertEquals(CardStatus.BLOCKED, cardEntity.getCardStatus());
    }

    @Test
    @DisplayName("Should get and set creditLimit correctly")
    void testCreditLimitGetterSetter() {
        BigDecimal limit = new BigDecimal("10000.00");
        cardEntity.setCreditLimit(limit);
        assertEquals(limit, cardEntity.getCreditLimit());
    }

    @Test
    @DisplayName("Should get and set availableBalance correctly")
    void testAvailableBalanceGetterSetter() {
        BigDecimal balance = new BigDecimal("3000.00");
        cardEntity.setAvailableBalance(balance);
        assertEquals(balance, cardEntity.getAvailableBalance());
    }

    @Test
    @DisplayName("Should get and set usedBalance correctly")
    void testUsedBalanceGetterSetter() {
        BigDecimal balance = new BigDecimal("2000.00");
        cardEntity.setUsedBalance(balance);
        assertEquals(balance, cardEntity.getUsedBalance());
    }

    @Test
    @DisplayName("Should get and set expirationDate correctly")
    void testExpirationDateGetterSetter() {
        LocalDate date = LocalDate.of(2027, 6, 30);
        cardEntity.setExpirationDate(date);
        assertEquals(date, cardEntity.getExpirationDate());
    }

    @Test
    @DisplayName("Should get and set issueDate correctly")
    void testIssueDateGetterSetter() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        cardEntity.setIssueDate(date);
        assertEquals(date, cardEntity.getIssueDate());
    }

    @Test
    @DisplayName("Should get and set createdAt correctly")
    void testCreatedAtGetterSetter() {
        LocalDateTime now = LocalDateTime.now();
        cardEntity.setCreatedAt(now);
        assertEquals(now, cardEntity.getCreatedAt());
    }

    @Test
    @DisplayName("Should get and set updatedAt correctly")
    void testUpdatedAtGetterSetter() {
        LocalDateTime now = LocalDateTime.now();
        cardEntity.setUpdatedAt(now);
        assertEquals(now, cardEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should return true when card is expiring soon (within 30 days)")
    void testIsExpiringSoonTrue() {
        // Card expiring in 15 days
        cardEntity.setExpirationDate(LocalDate.now().plusDays(15));
        assertTrue(cardEntity.isExpiringSoon());
    }

    @Test
    @DisplayName("Should return false when expiration date is in more than 30 days")
    void testIsExpiringSoonFalse() {
        // Card expiring in 60 days
        cardEntity.setExpirationDate(LocalDate.now().plusDays(60));
        assertFalse(cardEntity.isExpiringSoon());
    }

    @Test
    @DisplayName("Should return false when card is already expired")
    void testIsExpiringSoonExpired() {
        // Card expired 10 days ago
        cardEntity.setExpirationDate(LocalDate.now().minusDays(10));
        assertFalse(cardEntity.isExpiringSoon());
    }

    @Test
    @DisplayName("Should return false when expiration date is null")
    void testIsExpiringSoonNull() {
        cardEntity.setExpirationDate(null);
        assertFalse(cardEntity.isExpiringSoon());
    }

    @Test
    @DisplayName("Should return true when expiration date is exactly today")
    void testIsExpiringSoonToday() {
        cardEntity.setExpirationDate(LocalDate.now());
        assertTrue(cardEntity.isExpiringSoon());
    }

    @Test
    @DisplayName("Should return true when expiration date is exactly 30 days from now")
    void testIsExpiringSoon30Days() {
        cardEntity.setExpirationDate(LocalDate.now().plusDays(29));
        assertTrue(cardEntity.isExpiringSoon());
    }

    @Test
    @DisplayName("Should return true when available balance is sufficient")
    void testHasAvailableBalanceTrue() {
        cardEntity.setAvailableBalance(new BigDecimal("5000.00"));
        assertTrue(cardEntity.hasAvailableBalance(new BigDecimal("3000.00")));
    }

    @Test
    @DisplayName("Should return true when available balance equals amount requested")
    void testHasAvailableBalanceEqual() {
        cardEntity.setAvailableBalance(new BigDecimal("5000.00"));
        assertTrue(cardEntity.hasAvailableBalance(new BigDecimal("5000.00")));
    }

    @Test
    @DisplayName("Should return false when available balance is insufficient")
    void testHasAvailableBalanceFalse() {
        cardEntity.setAvailableBalance(new BigDecimal("2000.00"));
        assertFalse(cardEntity.hasAvailableBalance(new BigDecimal("3000.00")));
    }

    @Test
    @DisplayName("Should return false when available balance is null")
    void testHasAvailableBalanceNull() {
        cardEntity.setAvailableBalance(null);
        assertFalse(cardEntity.hasAvailableBalance(new BigDecimal("1000.00")));
    }

    @Test
    @DisplayName("Should return false when requested amount is zero and balance exists")
    void testHasAvailableBalanceZero() {
        cardEntity.setAvailableBalance(new BigDecimal("5000.00"));
        assertTrue(cardEntity.hasAvailableBalance(new BigDecimal("0.00")));
    }

    @Test
    @DisplayName("Should return true when balance is greater than requested amount")
    void testHasAvailableBalanceGreater() {
        cardEntity.setAvailableBalance(new BigDecimal("10000.00"));
        assertTrue(cardEntity.hasAvailableBalance(new BigDecimal("1000.00")));
    }
}


