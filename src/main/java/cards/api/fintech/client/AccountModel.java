package cards.api.fintech.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountModel(
        Long accountId,
        Long clientId,
        String firstName,
        String accountNumber,
        String currency,
        BigDecimal balance,
        BigDecimal balanceInPesos,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
