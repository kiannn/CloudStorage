package com.jwdnd.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credentials {
    
    Integer credentialId;
    String url;
    String username;
    String encodedPassword ;
    String password ;
    String keyEnc;
    Integer userId ;
    
    
}
