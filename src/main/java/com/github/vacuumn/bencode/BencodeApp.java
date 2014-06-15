package com.github.vacuumn.bencode;

import com.github.vacuumn.bencode.type.BElement;

import java.util.List;

/**
 * @author vacuumn@gmail.com
 */
public class BencodeApp
{
    public static void main( String[] args ) throws BencodingException {
        if(args == null || args.length != 1) {
            System.out.println("Please provide a valid bencoded string as input");
            System.exit(0);
        }
        BEncode bEncode = new BEncode();
        List<BElement> decode = bEncode.decode(args[0]);

        System.out.println("Decoded value: " + decode);
    }
}
