package com.jwdnd.cloudstorage.Mappers;

import com.jwdnd.cloudstorage.Model.User;
import com.jwdnd.cloudstorage.Model.UserCredentials;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USERS1 (salt, password, username, firstname, lastname) "
            + "VALUES(#{salt}, #{password}, #{userName}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    public Integer addUser(User build);

    @Select("SELECT userid FROM USERS1 WHERE username = #{username}")   
    public Integer getUserIdByUserName(String username);
    
    @Select("SELECT password, salt FROM USERS1 WHERE username = #{username}")   
    public UserCredentials getUserCredByUserName(String username);

}
