package com.trivia.core.utility;

import java.security.SecureRandom;
import java.util.*;

public final class Generator {
    private final static String RANDOM_ALGORITHM = "SHA1PRNG"; //TODO: For interoperability reasons use in production only.
    private final static String RANDOM_ALGORITHM_PROVIDER = "SUN";

    private Generator() {}

    /**
     * Is not truly random and it is not secure. Use only for non-security, non-critical operations.
     */
    public static int[] generateRandomUniqueArray(int size, int boundsMax) {
        int[] array = new int[size];
        Set<Integer> set = new HashSet<>();

        Random random = new Random();

        while (set.size() < size) {
            int randomNumber = random.nextInt(boundsMax);
            if (set.add(randomNumber)) {
                array[set.size() - 1] = randomNumber;
            }
        }

        return array;
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String generateSecureRandomString(int size) {
        int bytesSize = (int) Math.ceil(size * 3.0 / 4.0);     // Calculate how many bytes we need for a String of "size".
        byte[] bytes = generateSecureRandomBytes(bytesSize);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] generateSecureRandomBytes(int size) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[size];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
}