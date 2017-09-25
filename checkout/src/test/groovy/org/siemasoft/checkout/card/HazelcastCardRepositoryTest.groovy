package org.siemasoft.checkout.card

import com.hazelcast.core.IMap
import org.siemasoft.checkout.card.repository.CardRepository
import org.siemasoft.checkout.card.repository.HazelcastCardRepository
import org.siemasoft.checkout.card.repository.UnableToStoreCardException
import org.siemasoft.checkout.sumary.CardNotFoundException
import spock.lang.Specification

class HazelcastCardRepositoryTest extends Specification {

    IMap cards = Mock(IMap)

    CardRepository cardRepository = new HazelcastCardRepository(cards)

    def "should throws UnableToStoreCardException when storing card in hazelcast failed"() {
        given:
        Card card = Mock(Card)
        card.getId() >> "123:456"
        def cause = new NullPointerException("something bad happened")

        cards.put(card.getId(), card) >> { throw cause }

        when:
        cardRepository.store(card)

        then:
        UnableToStoreCardException exception = thrown(UnableToStoreCardException)
        exception.cause == cause
    }

    def "should store card in hazelcast"() {
        given:
        Card card = Mock(Card)
        card.getId() >> "123:456"


        when:
        cardRepository.store(card)

        then:
        1 * cards.put(card.getId(), card)
        noExceptionThrown()
    }

    def "should throws CardNotFoundException when card with given id does exist in hazelcast"() {
        given:
        String cardId = "123:456"

        cards.computeIfAbsent(cardId, _) >> { throw new CardNotFoundException(cardId) }

        when:
        cardRepository.findById(cardId)

        then:
        CardNotFoundException exception = thrown(CardNotFoundException)
        with(exception.errorResponse.details[0]) {
            field == "cardId"
            value == cardId
        }
    }

    def "should get card by given id from hazelcast"() {
        given:
        String cardId = "123:456"
        Card card = Mock(Card)

        cards.computeIfAbsent(cardId, _) >> card

        when:
        def foundCard = cardRepository.findById(cardId)

        then:
        foundCard == card
        noExceptionThrown()
    }
}