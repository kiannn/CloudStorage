package com.jwdnd.cloudstorage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.spec.GCMParameterSpec;

@Service
public class EncryptionService {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);

    private static final String ALGORITHM = "AES/GCM/NoPadding";

    private static final int IV_LENGTH = 12; // 96 bits
    private static final int TAG_LENGTH = 128; // authentication tag length in bits
    private final SecureRandom secureRandom = new SecureRandom();

    public EncryptionResult encryptValue(String data, String key) {

        try {
            // The master AES key is stored as Base64 text,
            // for example inside a Railway environment variable.
            // AES itself needs the actual binary key bytes,
            // so first we Base64-decode the stored key.
            byte[] keyBytes =Base64.getDecoder().decode(key);

            // Convert the raw key bytes into a SecretKey object
            // that Java's Cipher API understands.
            // "AES" tells Java that these bytes represent an AES key.
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

            // Create a new byte array that will hold the IV.
            // 12 bytes is the recommended size for GCM.
            byte[] iv = new byte[IV_LENGTH];


            // Fill the IV with cryptographically random bytes.
            // We generate a fresh IV every time we encrypt something.
            secureRandom.nextBytes(iv);

            // Java's GCM implementation:
            // - use a 128-bit authentication tag
            // - use the previously generated IV for this encryption
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH,iv);

            // Create a Cipher object configured for AES-GCM.
            Cipher cipher = Cipher.getInstance(ALGORITHM );

            // Initialize the cipher for ENCRYPTION.
            // We provide:
            // 1. encryption mode
            // 2. AES secret key
            // 3. GCM parameters including the IV
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,gcmParameterSpec);

            // Convert the plaintext String into UTF-8 byte array,
            // then encrypt those bytes.
            // doFinal() performs the actual encryption.
            // With GCM, the returned bytes also include
            // the authentication-tag information.
            byte[] encryptedBytes= cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));


            // Encrypted bytes can contain arbitrary binary values,
            // which may not be convenient to store in a VARCHAR column.
            // So we Base64-encode them into ordinary text.
            String encryptedValue =Base64.getEncoder().encodeToString(encryptedBytes);


            // The generated IV is also binary, so Base64-encode it
            // before storing it in the database.
            // The IV is NOT secret.
            String encodedIv =Base64.getEncoder().encodeToString(iv);

            // Encryption produces TWO values that we need:
            // 1. encrypted password/ciphertext
            // 2. IV used during that encryption
            // We return both inside EncryptionResult.
            return new EncryptionResult(encryptedValue,encodedIv);

        } catch (InvalidKeyException| NoSuchAlgorithmException |
                 BadPaddingException | NoSuchPaddingException  |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException  e) {

            throw new IllegalStateException("Unable to decrypt credential", e);
        }
    }

    public String decryptValue(String encryptedValue, String key, String encodedIv) {
        byte[] decryptedValue = null;

        try {
            
            // Recover the original AES key bytes
            // from the Base64 environment-variable value.
            byte[] keyBytes =Base64.getDecoder().decode(key);


            // Reconstruct the AES SecretKey object.
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");


            // The IV was stored in the database as Base64 text.
            // Decode it back into the original random bytes.
            byte[] iv = Base64.getDecoder().decode(encodedIv);

            // Recreate the exact same GCM configuration
            // that was used during encryption.
            // Decryption requires the SAME IV that was used
            // when this particular credential was encrypted.
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH,iv);

            // Create the AES-GCM Cipher again.
            Cipher cipher =Cipher.getInstance(ALGORITHM);

            // Initialize it this time for DECRYPTION.
            // We use:
            // - same master AES key
            // - same IV used during encryption
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);


            // The ciphertext was also stored as Base64 text.
            // Decode it back into the encrypted byte array.
            byte[] encryptedBytes =Base64.getDecoder().decode(encryptedValue);

            // Decrypt the encrypted bytes.
            // GCM also verifies the authentication tag here.
            // If the ciphertext or authentication information
            // was modified, doFinal() should fail.
            byte[] decryptedBytes =cipher.doFinal(encryptedBytes);

            // Convert the recovered plaintext bytes
            // back into the original Java String.
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException e) {

            throw new IllegalStateException( "Unable to decrypt credential", e);
        }
    }
}
