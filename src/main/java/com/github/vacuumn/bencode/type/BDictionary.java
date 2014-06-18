package com.github.vacuumn.bencode.type;

import com.google.common.collect.ImmutableSortedMap;

import java.util.Map;

/**
 * Bencoded Dictionaries implementation.
 * @see <a href="https://wiki.theory.org/BitTorrentSpecification#Dictionaries">Specification</a>
 *
 * @author vacuumn@gmail.com
 */
public class BDictionary implements BElement {
    private final Map<BString, BElement> elements;

    public BDictionary(Map<BString, BElement> elements) {
        //TODO: if perfomance is an issue, we can avoid this copying
        this.elements = ImmutableSortedMap.copyOf(elements);
    }

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append('d');
        for (Map.Entry<BString, BElement> entry : elements.entrySet()) {
            builder.append(entry.getKey().encode() + entry.getValue().encode());
        }
        return builder.append('e').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BDictionary that = (BDictionary) o;
        if (!elements.equals(that.elements)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public String toString() {
        return "BDictionary{" + "elements=" + elements + '}';
    }
}
