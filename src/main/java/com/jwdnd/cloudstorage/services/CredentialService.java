package com.jwdnd.cloudstorage.services;

import com.jwdnd.cloudstorage.Mappers.CredentialsMapper;
import com.jwdnd.cloudstorage.Mappers.UserMapper;
import com.jwdnd.cloudstorage.Model.Credentials;
import com.jwdnd.cloudstorage.Model.User;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    @Autowired
    private UserMapper UserMapper; 
    
    @Autowired
    private EncryptionService EncryptionService;
    
    @Autowired
    private CredentialsMapper CredentialsMapper;
            
    public List<Credentials> getAllCredentials(String user) {

        User userByUserName = UserMapper.getUserByUserName(user);

        List<Credentials> allCredential = CredentialsMapper.findAllCredentials(userByUserName.getUserid());

        allCredential.forEach(cred -> {
            String decryptValue = EncryptionService.decryptValue(cred.getEncodedPassword(), cred.getKeyEnc());

            cred.setPassword(decryptValue);

        });
        return allCredential;

    }

    public void deleteCredentials(Integer credentialId) {
        
           CredentialsMapper.deletCredential(credentialId); 

    }

    public boolean isValidCredId(String username, Integer credentialsId) {
        
        Collection<Credentials> allCredential = getAllCredentials(username);

        List<Integer> fileIds = allCredential.stream()
                                             .map(cred -> cred.getCredentialId())
                                             .collect(Collectors.toList());
        
        boolean contains = fileIds.contains(credentialsId);
        
        return contains;

    }

    public void updateCredentials(Credentials Credentials) {

        String password = Credentials.getPassword();

        String generatedEncodedKey = generateEncodedKey();

        String encryptedPassword = EncryptionService.encryptValue(password, generatedEncodedKey);

        Credentials.setEncodedPassword(encryptedPassword);
        Credentials.setKeyEnc(generatedEncodedKey);

        CredentialsMapper.updateCredential(Credentials);

    }

    public void addCredential(Credentials Credentials, String name) {
        
        User userByUserName = UserMapper.getUserByUserName(name);
        
        String password = Credentials.getPassword();
        String generatedEncodedKey = generateEncodedKey();
        
        String encryptedPassword = EncryptionService.encryptValue(password, generatedEncodedKey);
        
        Credentials build = Credentials.builder()
                                       .url(Credentials.getUrl())
                                       .username(Credentials.getUsername())
                                       .encodedPassword(encryptedPassword)
                                       .keyEnc(generatedEncodedKey)
                                       .userId(userByUserName.getUserid())
                                       .build();
 
        CredentialsMapper.addCredential(build);

    }

    public String generateEncodedKey() {
        
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        return encodedKey;
    }

}
