package com.jwdnd.cloudstorage.services;

import com.jwdnd.cloudstorage.Mappers.UserMapper;
import com.jwdnd.cloudstorage.Model.User;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper UserMapper;
    
    @Autowired
    private HashService hashService;

    public void createNewUSer(User User) {
        
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        
        String hashedPassword = hashService.getHashedValue(User.getPassword(), encodedSalt);
        
        User build = User.builder()
                         .firstName(User.getFirstName().trim())
                         .lastName(User.getLastName().trim())
                         .userName(User.getUserName().trim())
                         .salt(encodedSalt)
                         .password(hashedPassword)
                         .build();

        UserMapper.addUser(build);

    }

    public boolean userExist(String username) {

        User usernameExsit = UserMapper.getUserByUserName(username);
        boolean exist = usernameExsit==null;
        
        return exist;

    }
    
    public User findUserByUserName(String username){
    
        User userByUserName = UserMapper.getUserByUserName(username);
        
        return userByUserName;
    }
}
