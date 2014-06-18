package com.github.vacuumn.bencode.type;

/**
 * Bencoded Strings implementation.
 * @see <a href="http://wiki.theory.org/BitTorrentSpecification#Bencoded_Strings">Specification</a>
 *
 * @author vacuumn@gmail.com
 */
public class BString implements BElement,Comparable<BString> {
    private final String value;

    public BString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String encode() {
        return value.length() + ":" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BString bString = (BString) o;

        if (value != null ? !value.equals(bString.value) : bString.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(BString o) {
        return this.value.compareTo(o.value);
    }
}
