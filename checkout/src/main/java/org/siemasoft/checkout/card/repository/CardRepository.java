package org.siemasoft.checkout.card.repository;

import org.siemasoft.checkout.card.Card;
import org.siemasoft.checkout.sumary.CardNotFoundException;

public interface CardRepository {

    void store(Card card) throws UnableToStoreCardException;

    Card findById(String cardId) throws CardNotFoundException;
}
