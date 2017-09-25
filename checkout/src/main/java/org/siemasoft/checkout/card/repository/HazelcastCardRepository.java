package org.siemasoft.checkout.card.repository;

import com.hazelcast.core.IMap;
import lombok.RequiredArgsConstructor;
import org.siemasoft.checkout.card.Card;
import org.siemasoft.checkout.sumary.CardNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HazelcastCardRepository implements CardRepository {

    private final IMap<String, Card> cards;

    @Override
    public void store(Card card) {
        try {
            cards.put(card.getId(), card);
        } catch (Exception e) {
            throw new UnableToStoreCardException(e);
        }
    }

    @Override
    public Card findById(String cardId) {
        return cards.computeIfAbsent(cardId, key -> {
            throw new CardNotFoundException(cardId);
        });
    }
}
