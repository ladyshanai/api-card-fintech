package cards.api.fintech.dto;

import cards.api.fintech.enums.CardBrand;
import cards.api.fintech.enums.CardType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardRequest(
        Long accountId,
        CardType cardType,
        CardBrand cardBrand,
        BigDecimal creditLimit,
        BigDecimal availableBalance,
        BigDecimal usedBalance,
        LocalDate expirationDate,
        LocalDate issueDate) {
}
