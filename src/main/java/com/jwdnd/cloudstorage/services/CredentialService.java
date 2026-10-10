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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    @Autowired
    private UserMapper UserMapper; 
    
    @Autowired
    private EncryptionService EncryptionService;
    
    @Autowired
    private CredentialsMapper CredentialsMapper;
    
    @Value("${app.encryption.key}")
    private String masterEncryptionKey;
            
    public List<Credentials> getAllCredentials(String user) {

        Integer userByUserName = UserMapper.getUserIdByUserName(user);

        List<Credentials> allCredential = CredentialsMapper.findAllCredentials(userByUserName);

        allCredential.forEach(cred -> {
            
            String decryptValue = EncryptionService.decryptValue(cred.getEncodedPassword(), masterEncryptionKey, cred.getIv());

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

        EncryptionResult encryptedPassword = EncryptionService.encryptValue(password, masterEncryptionKey);

        Credentials.setEncodedPassword(encryptedPassword.getEncryptedValue());
        Credentials.setIv(encryptedPassword.getIv());

        CredentialsMapper.updateCredential(Credentials);

    }

    public void addCredential(Credentials Credentials, String name) {
        
        Integer userByUserName = UserMapper.getUserIdByUserName(name);
        
        String password = Credentials.getPassword();
        
        EncryptionResult encryptedPassword = EncryptionService.encryptValue(password, masterEncryptionKey);
        
        Credentials build = Credentials.builder()
                                       .url(Credentials.getUrl())
                                       .username(Credentials.getUsername())
                                       .encodedPassword(encryptedPassword.getEncryptedValue())
                                       .iv(encryptedPassword.getIv())
                                       .userId(userByUserName)
                                       .build();
 
        CredentialsMapper.addCredential(build);

    }
}
