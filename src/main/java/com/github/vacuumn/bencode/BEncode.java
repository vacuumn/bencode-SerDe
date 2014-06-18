package com.github.vacuumn.bencode;

import com.github.vacuumn.bencode.type.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main entry point for encoding-decoding operations.
 *
 * @author vacuumn@gmail.com
 */
public class BEncode {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Encode any known BElement to bencode format.
     * @param bElement element to endode
     * @return bencoded string
     */
    public String encode(BElement bElement) {
        logger.info("Encoding element: {}", bElement);
        String encoded = bElement.encode();
        logger.info("Finished encoding bElement : {}, result: {}", bElement, encoded);
        return encoded;
    }

    /**
     * Decode string to known elements.
     * @param encoded bencoded string to decode
     * @return list of decoded elements
     * @throws BencodingException in case of decoding error or wrong input string
     */
    public List<BElement> decode(String encoded) throws BencodingException {
        logger.info("Decoding string : {}", encoded);
        if (encoded == null) return null;
        AtomicInteger index = new AtomicInteger(0);
        List<BElement> elements = Lists.newArrayList();

        try {
            while (encoded.length() > index.get()) {
                elements.add(decodeElement(encoded, index));
            }
            if (index.get() != encoded.length())
                throw new BencodingException("invalid bencoded value (data after valid prefix)");
            logger.info("Finished decoding string : {}, result: {}", encoded, elements);
            return elements;
        } catch (BencodingException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    private BElement decodeElement(String bencodedString, AtomicInteger index) throws BencodingException {
        switch (bencodedString.charAt(index.get())) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readString(bencodedString, index);
            case 'i':
                return readInteger(bencodedString, index);
            case 'l':
                return readList(bencodedString, index);
            case 'd':
                return readDictionary(bencodedString, index);
            default:
                throw new BencodingException("Error while encoding", bencodedString);
        }
    }

    private BDictionary readDictionary(String bencodedString, AtomicInteger index) throws BencodingException {
        if (bencodedString.charAt(index.get()) == 'd') index.set(index.get() + 1);
        Map<BString, BElement> elements = Maps.newHashMap();
        try {
            while (bencodedString.charAt(index.get()) != 'e') {
                BString key = readString(bencodedString, index);
                BElement value = decodeElement(bencodedString, index);
                elements.put(key, value);
            }
        } catch (Exception e) {
            throw new BencodingException("Error while encoding dictionary", e, bencodedString);
        }
        index.set(index.get() + 1);
        return new BDictionary(elements);
    }

    private BList readList(String bencodedString, AtomicInteger index) throws BencodingException {
        if (bencodedString.charAt(index.get()) == 'l') index.set(index.get() + 1);

        List elements = Lists.newArrayList();
        try {
            while (bencodedString.charAt(index.get()) != 'e') {
                elements.add(decodeElement(bencodedString, index));
            }
        } catch (Exception e) {
            throw new BencodingException("Error while encoding integer", e, bencodedString);
        }
        index.set(index.get() + 1);
        return new BList(elements);
    }

    private BInteger readInteger(String bencodedString, AtomicInteger index) throws BencodingException {
        index.incrementAndGet();

        int end = bencodedString.indexOf('e', index.get());

        if (end == -1) throw new BencodingException("Error while encoding integer");

        try {
            Integer integer = Integer.valueOf(bencodedString.substring(index.get(), end));
            index.set(end + 1);
            return new BInteger(integer);
        } catch (Exception e) {
            throw new BencodingException("Error while encoding integer", e, bencodedString);
        }
    }

    private BString readString(String bencodedString, AtomicInteger index) throws BencodingException {
        try {
            int colonIndex = bencodedString.indexOf(':', index.get());
            int length = Integer.valueOf(bencodedString.substring(index.get(), colonIndex));
            index.set(colonIndex + 1);
            String value = bencodedString.substring(index.get(), index.get() + length);
            index.set(index.get() + length);
            return new BString(value);
        } catch (Exception e) {
            throw new BencodingException("Error while encoding string", e, bencodedString);
        }
    }
}
