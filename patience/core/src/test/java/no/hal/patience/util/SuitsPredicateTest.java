package no.hal.patience.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.hal.patience.Card;
import no.hal.patience.SuitKind;

public class SuitsPredicateTest {

    @Test
    public void testSameSuit_empty() {
        assertTrue(SuitsPredicate.sameSuit(null).test(List.of()));
    }

    @Test
    public void testSameAsS_SSSSucceeds() {
        assertTrue(SuitsPredicate.sameAs(SuitKind.spades).test(Card.cards("S3", "S2", "SK")));
    }

    @Test
    public void testSameAsS_SDSFails() {
        assertFalse(SuitsPredicate.sameAs(SuitKind.spades).test(Card.cards("S3", "D2", "SK")));
    }

    @Test
    public void testSame_DDDSucceeds() {
        assertTrue(SuitsPredicate.same().test(Card.cards("D3", "D2", "DK")));
    }

    @Test
    public void testAlternatingColor_DSHCSucceeds() {
        assertTrue(SuitsPredicate.alternatingColor().test(Card.cards("D1", "S7", "H2", "C3")));
    }

    @Test
    public void testAlternatingColor_DSCHFails() {
        assertFalse(SuitsPredicate.alternatingColor().test(Card.cards("D1", "S7", "C3", "H2")));
    }
}
