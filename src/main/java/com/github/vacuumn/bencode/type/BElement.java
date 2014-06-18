package com.github.vacuumn.bencode.type;

/**
 * Any element that can be ecoded and decoded in Bencode format
 * @author vacuumn@gmail.com
 */
public interface BElement {

    /**
     * Encoding format is desribed in specification.
     * Please, refer to suitable specification link in concrete BElement implementation.
     * @return bencoded string of this objet
     */
    public String encode();
}
