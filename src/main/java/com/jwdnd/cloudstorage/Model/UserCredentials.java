package com.jwdnd.cloudstorage.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentials {

    String password;
    String salt;

}
