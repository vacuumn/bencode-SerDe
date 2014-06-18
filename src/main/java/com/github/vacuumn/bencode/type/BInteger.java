package com.github.vacuumn.bencode.type;

/**
 * Bencoded Integers implementation.
 * @see <a href="https://wiki.theory.org/BitTorrentSpecification#Integers">Specification</a>
 *
 * @author vacuumn@gmail.com
 */
public class BInteger implements BElement {
    private int value;

    public BInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public String encode() {
        return "i" + value + "e";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BInteger bInteger = (BInteger) o;

        if (value != bInteger.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
