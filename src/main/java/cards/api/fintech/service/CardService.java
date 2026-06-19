package cards.api.fintech.service;

import cards.api.fintech.client.AccountClient;
import cards.api.fintech.dto.CardRequest;
import cards.api.fintech.dto.CardResponse;
import cards.api.fintech.entity.CardEntity;
import cards.api.fintech.enums.CardStatus;
import cards.api.fintech.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountClient accountClient;

    public CardService(CardRepository cardRepository, AccountClient accountClient) {
        this.cardRepository = cardRepository;
        this.accountClient = accountClient;
    }

    public CardResponse createCard(CardRequest cardRequest) {

        var account = accountClient.getAccountById(cardRequest.accountId());
        var cardNumber = generateCardNumber();
        var cardEntity = new CardEntity();
        cardEntity.setAccountId(account.accountId());
        cardEntity.setCardNumber(cardNumber);
        cardEntity.setCardType(cardRequest.cardType());
        cardEntity.setCardBrand(cardRequest.cardBrand());
        cardEntity.setCardStatus(CardStatus.ACTIVE);
        cardEntity.setCreditLimit(cardRequest.creditLimit());
        cardEntity.setAvailableBalance(cardRequest.availableBalance());
        cardEntity.setUsedBalance(cardRequest.usedBalance());
        cardEntity.setExpirationDate(cardRequest.expirationDate());
        cardEntity.setIssueDate(cardRequest.issueDate());
        cardEntity.setCreatedAt(LocalDateTime.now());
        cardEntity.setUpdatedAt(LocalDateTime.now());

        var savedCard = cardRepository.save(cardEntity);

        return mapToResponse(savedCard);
    }

    public List<CardResponse> getCards() {
        return cardRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CardResponse getCardById(Long id) {
        var cardEntity = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));

        return mapToResponse(cardEntity);
    }

    public List<CardResponse> getCardsByAccount(Long accountId) {
        return cardRepository.findByAccountId(accountId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CardResponse> getCardsByStatus(CardStatus status) {
        return cardRepository.findByCardStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CardResponse updateStatus(Long id, CardStatus newStatus) {
        var cardEntity = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));

        cardEntity.setCardStatus(newStatus);
        cardEntity.setUpdatedAt(LocalDateTime.now());

        var updatedCard = cardRepository.save(cardEntity);

        return mapToResponse(updatedCard);
    }

    public void deleteById(Long id) {
        var cardEntity = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));

        cardRepository.delete(cardEntity);
    }

    private CardResponse mapToResponse(CardEntity cardEntity) {
        return new CardResponse(
                cardEntity.getId(),
                cardEntity.getAccountId(),
                cardEntity.getCardNumber(),
                cardEntity.getCardType(),
                cardEntity.getCardStatus(),
                cardEntity.getCardBrand(),
                cardEntity.getCreditLimit(),
                cardEntity.getAvailableBalance(),
                cardEntity.getUsedBalance(),
                cardEntity.getExpirationDate(),
                cardEntity.getIssueDate(),
                cardEntity.getCreatedAt(),
                cardEntity.getUpdatedAt(),
                cardEntity.isExpiringSoon()
        );
    }

    private String generateCardNumber() {
        return "4509" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);
    }
}