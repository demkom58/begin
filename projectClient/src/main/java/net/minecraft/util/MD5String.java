package net.minecraft.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5String {
    private String salt;

    public MD5String(String salt) {
        this.salt = salt;
    }

    public String hash(String input) {
        try {
            final String valueAndSalt = this.salt + input;

            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(valueAndSalt.getBytes(), 0, valueAndSalt.length());

            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
