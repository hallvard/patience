package no.hal.patience.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.hal.patience.Card;
import no.hal.patience.CardOrder;

public class FacesPredicateTest {

    @Test
    public void testStepping_empty() {
        assertTrue(FacesPredicate.stepping(3, 89, CardOrder.identity()).test(List.of()));
    }

    @Test
    public void testIncreasingFrom3_3To5Succeeds() {
        assertTrue(FacesPredicate.increasingFrom(3).test(Card.cards("S3", "C4", "D5")));
    }

    @Test
    public void testIncreasingFrom3_1To3Fails() {
        assertFalse(FacesPredicate.increasingFrom(3).test(Card.cards("S1", "C2", "D3")));
    }

    @Test
    public void testIncreasing_3To5Succeeds() {
        assertTrue(FacesPredicate.increasing().test(Card.cards("S3", "C4", "D5")));
    }

    @Test
    public void testIncreasing_3To1Fails() {
        assertFalse(FacesPredicate.increasing().test(Card.cards("S3", "C2", "D1")));
    }

    @Test
    public void testDecreasingFrom5_5To3Succeeds() {
        assertTrue(FacesPredicate.decreasingFrom(5).test(Card.cards("S5", "C4", "D3")));
    }

    @Test
    public void testDecreasingFrom5_3To1Fails() {
        assertFalse(FacesPredicate.decreasingFrom(5).test(Card.cards("S3", "C2", "D1")));
    }

    @Test
    public void testDecreasing_5To3Succeeds() {
        assertTrue(FacesPredicate.decreasing().test(Card.cards("S5", "C4", "D3")));
    }

    @Test
    public void testDecreasing_1To3Fails() {
        assertFalse(FacesPredicate.decreasing().test(Card.cards("S1", "C2", "D3")));
    }

    @Test
    public void testSameAs5_555Succeeds() {
        assertTrue(FacesPredicate.sameAs(5).test(Card.cards("S5", "C5", "D5")));
    }

    @Test
    public void testSameAs5_545Fails() {
        assertFalse(FacesPredicate.sameAs(5).test(Card.cards("S5", "C4", "D5")));
    }

    @Test
    public void testSame_555Succeeds() {
        assertTrue(FacesPredicate.sameAs(5).test(Card.cards("S5", "C5", "D5")));
    }

    @Test
    public void testSame_545Fails() {
        assertFalse(FacesPredicate.sameAs(5).test(Card.cards("S5", "C4", "D5")));
    }
}
