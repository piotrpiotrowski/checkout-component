package org.siemasoft.checkout.sumary;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.siemasoft.checkout.card.Card;
import org.siemasoft.checkout.card.repository.CardRepository;
import org.siemasoft.checkout.dto.OrderDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final CardRepository cardRepository;

    public OrderDto getCustomerOrder(long customerId, String cardId) {
        return Optional.ofNullable(cardRepository.findById(cardId))
                       .filter(card -> card.isBelongingToCustomer(customerId))
                       .map(Card::asOrder)
                       .orElseThrow(() -> new CardAccessDeniedException(cardId, customerId));
    }
}