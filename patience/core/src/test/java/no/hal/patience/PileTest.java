package no.hal.patience;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.hal.patience.util.CardsPredicate;

public class PileTest {
    
    private final Card
        s1 = Card.valueOf(CardKind.S1),
        h2 = Card.valueOf(CardKind.H2),
        d4 = Card.valueOf(CardKind.D4),
        c3 = Card.valueOf(CardKind.C3);

    private void assertCards(Iterable<Card> pile, Card... cards) {
        int pos = 0;
        for (Card card : pile) {
            assertSame(cards[pos], card);
            pos++;
        }
    }

    @Test
    public void testPile_S1H2() {
        assertCards(new Pile(CardsPredicate.whatever, List.of(s1, h2)), s1, h2);
    }

    // replacedCards/replaceCards

    @Test
    public void testReplacedCards_toEnd() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        assertCards(pile.replacedCards(1, 2, List.of(d4, c3)), s1, d4, c3);
    }
    @Test
    public void testReplaceCards_toEnd() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.replaceCards(1, 2, List.of(d4, c3));
        assertCards(pile, s1, d4, c3);
    }

    @Test
    public void testReplacedCards_fromStart() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        assertCards(pile.replacedCards(0, 1, List.of(d4, c3)), d4, c3, h2);
    }
    @Test
    public void testReplaceCards_fromStart() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.replaceCards(0, 1, List.of(d4, c3));
        assertCards(pile, d4, c3, h2);
    }

    // addedCards/addCards

    @Test
    public void testAddedCards() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        assertCards(pile.addedCards(List.of(d4, c3)), s1, h2, d4, c3);
    }
    @Test
    public void testAddCards() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.addCards(List.of(d4, c3));
        assertCards(pile, s1, h2, d4, c3);
    }

    // insertedCards/insertCard

    @Test
    public void testInsertedCards_middle() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        assertCards(pile.insertedCards(1, List.of(d4, c3)), s1, d4, c3, h2);
    }
    @Test
    public void testInsertCards_middle() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.insertCards(1, List.of(d4, c3));
        assertCards(pile, s1, d4, c3, h2);
    }

    // takeCards/testTakeCardsReversed/testTakeCard

    @Test
    public void testTakeCards() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2, d4));
        assertCards(pile.takeCards(2), h2, d4);
        assertCards(pile, s1);
    }

    @Test
    public void testTakeCardsReversed() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2, d4));
        assertCards(pile.takeCardsReversed(2), d4, h2);
        assertCards(pile, s1);
    }

    @Test
    public void testTakeCard() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2, d4));
        assertSame(pile.takeCard(), d4);
        assertCards(pile, s1, h2);
    }

    // moveCards

    @Test
    public void testMoveCards() {
        Pile source = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        Pile target = new Pile(CardsPredicate.whatever, List.of(d4));
        MoveCardsOperation.moveCards(source, target, 2);
        assertCards(source);
        assertCards(target, d4, s1, h2);
    }

    @Test
    public void testMoveCardsReversed() {
        Pile source = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        Pile target = new Pile(CardsPredicate.whatever, List.of(d4));
        MoveCardsOperation.moveCardsReversed(source, target, 2);
        assertCards(source);
        assertCards(target, d4, h2, s1);
    }
}
