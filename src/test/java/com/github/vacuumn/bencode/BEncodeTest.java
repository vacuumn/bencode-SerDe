package com.github.vacuumn.bencode;

import com.github.vacuumn.bencode.type.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test cases taken from the original BitTorrent implementation
 *
 * @author vacuumn@gmail.com
 */
public class BEncodeTest {

    private List<Map.Entry<? extends BElement, String>> knownValues = Arrays.asList(
            Maps.immutableEntry(new BInteger(0), "i0e"),
            Maps.immutableEntry(new BInteger(1), "i1e"),
            Maps.immutableEntry(new BInteger(10), "i10e"),
            Maps.immutableEntry(new BInteger(42), "i42e"),
            Maps.immutableEntry(new BInteger(-42), "i-42e"),
            Maps.immutableEntry(new BString("spam"), "4:spam"),
            Maps.immutableEntry(new BString("parrot sketch"), "13:parrot sketch"),
            Maps.immutableEntry(new BList(Arrays.asList(new BString("parrot sketch"), new BInteger(42))),
                    "l13:parrot sketchi42ee"),
            Maps.immutableEntry(new BDictionary(ImmutableMap.of(
                            new BString("foo"), new BInteger(42),
                            new BString("bar"), new BString("spam"))),
                    "d3:bar4:spam3:fooi42ee")
    );

    private BEncode bEncode = new BEncode();

    @Test
    public void testDecodeKnownValues() throws Exception {
        //decode should give known result with known input
        for (Map.Entry<? extends BElement, String> knownValue : knownValues) {
            List<BElement> result = bEncode.decode(knownValue.getValue());
            System.out.println(knownValue.getValue() + " result: " + result.get(0));
            assertEquals(knownValue.getKey(), result.iterator().next());
        }
    }

    @Test
    public void testEncodeKnownValues() throws Exception {
        //encode should give known result with known input
        for (Map.Entry<? extends BElement, String> knownValue : knownValues) {
            String result = bEncode.encode(knownValue.getKey());
            System.out.println(knownValue.getValue() + " result: " + result);
            assertEquals(knownValue.getValue(), result);
        }
    }

    @Test
    public void testRoundtripEncoded() throws Exception {
        //consecutive calls to decode and encode should deliver the original data again
        for (Map.Entry<? extends BElement, String> knownValue : knownValues) {
            List<BElement> result = bEncode.decode(knownValue.getValue());
            assertEquals(knownValue.getValue(), bEncode.encode(result.iterator().next()));
        }
    }

    @Test
    public void testRoundtripDecoded() throws Exception {
        //consecutive calls to encode and decode should deliver the original  data again
        for (Map.Entry<? extends BElement, String> knownValue : knownValues) {
            String result = bEncode.encode(knownValue.getKey());
            assertEquals(knownValue.getKey(), bEncode.decode(result).iterator().next());
        }
    }

    //illegally formatted strings should raise an exception when ecoded

    @Test(expected = BencodingException.class)
    public void testBdecodeExceptinOnInvalidBencodedValue() throws Exception {
        bEncode.decode("4:spammmmmm");
    }

    @Test(expected = BencodingException.class)
    public void testRaiseIllegalInputForDecode1() throws Exception {
        bEncode.decode("foo");
    }

    @Test(expected = BencodingException.class)
    public void testRaiseIllegalInputForDecode2() throws Exception {
        bEncode.decode("x:foo");
    }

    @Test(expected = BencodingException.class)
    public void testRaiseIllegalInputForDecode3() throws Exception {
        bEncode.decode("x42e");
    }

    @Test
    public void testSortedKeysForDicts() throws Exception {
        //the keys of a dictionary must be sorted before encoded
        BDictionary dict = new BDictionary(ImmutableMap.of(
                new BString("zoo"), new BInteger(42),
                new BString("bar"), new BString("spam")));
        String encodeResult = bEncode.encode(dict);
        assertTrue(encodeResult.indexOf("zoo") > encodeResult.indexOf("bar"));
    }

    @Test
    public void testNestedDictionary() throws Exception {
        // tests for handling of nested dicts
        BDictionary dict = new BDictionary(ImmutableMap.of(
                new BString("foo"), new BInteger(42),
                new BString("bar"), new BDictionary(ImmutableMap.of(
                        new BString("sketch"), new BString("parrot"),
                        new BString("foobar"), new BInteger(23)))));
        String encodeResult = bEncode.encode(dict);
        assertEquals("d3:bard6:foobari23e6:sketch6:parrote3:fooi42ee", encodeResult);
    }

}
