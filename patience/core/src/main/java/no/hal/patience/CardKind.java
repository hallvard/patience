package no.hal.patience;

import static no.hal.patience.SuitKind.spades;
import static no.hal.patience.SuitKind.hearts;
import static no.hal.patience.SuitKind.diamonds;
import static no.hal.patience.SuitKind.clubs;

public enum CardKind {
    
    S1(spades, 1), S2(spades, 2), S3(spades, 3), S4(spades, 4), S5(spades, 5), S6(spades, 6), S7(spades, 7), S8(spades, 8), S9(spades, 9), S10(spades, 10), SJ(spades, 11), SQ(spades, 12), SK(spades, 13),
    H1(hearts, 1), H2(hearts, 2), H3(hearts, 3), H4(hearts, 4), H5(hearts, 5), H6(hearts, 6), H7(hearts, 7), H8(hearts, 8), H9(hearts, 9), H10(hearts, 10), HJ(hearts, 11), HQ(hearts, 12), HK(hearts, 13),
    D1(diamonds, 1), D2(diamonds, 2), D3(diamonds, 3), D4(diamonds, 4), D5(diamonds, 5), D6(diamonds, 6), D7(diamonds, 7), D8(diamonds, 8), D9(diamonds, 9), D10(diamonds, 10), DJ(diamonds, 11), DQ(diamonds, 12), DK(diamonds, 13),
    C1(clubs, 1), C2(clubs, 2), C3(clubs, 3), C4(clubs, 4), C5(clubs, 5), C6(clubs, 6), C7(clubs, 7), C8(clubs, 8), C9(clubs, 9), C10(clubs, 10), CJ(clubs, 11), CQ(clubs, 12), CK(clubs, 13)
    ;

    private final SuitKind suit;
    private int face;

    private CardKind(SuitKind suit, int face) {
        this.suit = suit;
        this.face = face;
    }

    public SuitKind getSuit() {
        return suit;
    }

    public int getFace() {
        return face;
    }

    public boolean isOppositeColor(CardKind cardKind) {
        return suit.isOppositeColor(cardKind.suit);
    }

    //

    public final static CardKind[]
        allSpades = { S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, SJ, SQ, SK },
        allHearts = { H1, H2, H3, H4, H5, H6, H7, H8, H9, H10, HJ, HQ, HK },
        allDiamonds = { D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, DJ, DQ, DK },
        allClubs = { C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, CJ, CQ, CK };
}
