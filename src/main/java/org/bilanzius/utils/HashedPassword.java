package org.bilanzius.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class HashedPassword {

    private static final String ALGORITHM = "SHA-512";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static HashedPassword fromPlainText(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);

            byte[] hashedPassword = digest.digest(plainPassword.getBytes(DEFAULT_CHARSET));
            return new HashedPassword(new String(hashedPassword, DEFAULT_CHARSET));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static HashedPassword fromHashedText(String hashedPassword) {
        return new HashedPassword(hashedPassword);
    }

    private final String password;

    private HashedPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashedPassword that = (HashedPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }

    public String getPassword() {
        return password;
    }
}
