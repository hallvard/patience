package no.hal.patience.util;

public class Pair<T1, T2> {
    
    private final T1 o1;
    private final T2 o2;

    public Pair(T1 o1, T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public T1 get1() {
        return o1;
    }

    public T2 get2() {
        return o2;
    }
}