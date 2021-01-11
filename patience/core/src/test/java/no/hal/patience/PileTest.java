package no.hal.patience;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;

public class PileTest {
    
    private final Card
        s1 = Card.valueOf(CardKind.S1),
        h2 = Card.valueOf(CardKind.H2),
        d4 = Card.valueOf(CardKind.D4),
        c3 = Card.valueOf(CardKind.C3);

    private void assertCards(Pile pile, Card... cards) {
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

    @Test
    public void testReplaceCards_toEnd() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.replaceCards(1, 2, List.of(d4, c3));
        assertCards(pile, s1, d4, c3);
    }

    @Test
    public void testReplaceCards_fromStart() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.replaceCards(0, 1, List.of(d4, c3));
        assertCards(pile, d4, c3, h2);
    }

    @Test
    public void testInsertCards_middle() {
        Pile pile = new Pile(CardsPredicate.whatever, List.of(s1, h2));
        pile.insertCards(1, List.of(d4, c3));
        assertCards(pile, s1, d4, c3, h2);
    }
}
