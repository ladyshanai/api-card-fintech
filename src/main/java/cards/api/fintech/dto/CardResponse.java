package cards.api.fintech.dto;

import cards.api.fintech.enums.CardBrand;
import cards.api.fintech.enums.CardStatus;
import cards.api.fintech.enums.CardType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CardResponse(
        Long id,
        Long accountId,
        String cardNumber,
        CardType cardType,
        CardStatus cardStatus,
        CardBrand cardBrand,
        BigDecimal creditLimit,
        BigDecimal availableBalance,
        BigDecimal usedBalance,
        LocalDate expirationDate,
        LocalDate issueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean expiringSoon
) {
}