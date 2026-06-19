package cards.api.fintech.repository;

import cards.api.fintech.entity.CardEntity;
import cards.api.fintech.enums.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByAccountId(Long accountId);

    List<CardEntity> findByCardStatus(CardStatus cardStatus);
}
