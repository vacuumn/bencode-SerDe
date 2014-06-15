package com.github.vacuumn.bencode.type;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author vacuumn@gmail.com
 */
public class BList implements BElement {

   private final List<BElement> items;

    public BList(List<BElement> elements) {
        //TODO: if perfomance is an issue, we can avoid this copying
        items = ImmutableList.copyOf(elements);
    }

    public int count() {
        return items.size();
    }

    public List<BElement> getItems() {
        return items;
    }

    @Override
    public String encode() {
        final StringBuilder builder = new StringBuilder();
        builder.append('l');
        for (final BElement element : this.items) {
            builder.append(element.encode());
        }
        return builder.append('e').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BList bList = (BList) o;

        if (items != null ? !items.equals(bList.items) : bList.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BList{" +
                "items=" + items +
                '}';
    }
}
