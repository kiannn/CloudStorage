package com.jwdnd.cloudstorage.services;

public class EncryptionResult {

    private final String encryptedValue;
    private final String iv;

    public EncryptionResult(String encryptedValue, String iv) {
        
        this.encryptedValue = encryptedValue;
        this.iv = iv;
    }

    public String getEncryptedValue() {
        return encryptedValue;
    }

    public String getIv() {
        return iv;
    }
}
