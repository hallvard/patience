package no.hal.patience;

public enum ColorKind {
    black, red;

    public ColorKind getOpposite() {
        return switch (this) {
            case red -> black;
            case black -> red;
        };
    }
}