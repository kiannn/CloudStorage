package com.jwdnd.cloudstorage.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class User {
    
  Integer userid ;
  String salt ;
  String password ;
  String userName ;
  String firstName ;
  String lastName;

    @Override
    public String toString() {
        return "User{" + "userid=" + userid + ", salt=" + salt + ", password=" + password + ", userName=" + userName + ", firstName=" + firstName + ", lastname=" + lastName + '}';
    }

    
}
