package com.jwdnd.cloudstorage.Mappers;

import com.jwdnd.cloudstorage.Model.User;
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

    @Select("SELECT * FROM USERS1 WHERE username = #{username}")
    public User getUserByUserName(String username);

}
