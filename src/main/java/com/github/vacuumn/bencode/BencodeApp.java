package com.github.vacuumn.bencode;

import com.github.vacuumn.bencode.type.BElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author vacuumn@gmail.com
 */
public class BencodeApp {

    private static Logger logger = LoggerFactory.getLogger(BencodeApp.class);

    public static void main(String[] args) throws BencodingException {
        if (args == null || args.length != 1) {
            logger.error("Please provide a valid bencoded string as input");
            System.exit(0);
        }
        BEncode bEncode = new BEncode();
        List<BElement> decode = bEncode.decode(args[0]);

        logger.info("Decoded value: " + decode);
    }
}
