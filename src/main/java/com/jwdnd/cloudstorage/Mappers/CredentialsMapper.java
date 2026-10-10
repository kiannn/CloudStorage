
package com.jwdnd.cloudstorage.Mappers;

import com.jwdnd.cloudstorage.Model.Credentials;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CredentialsMapper {

    @Insert("""
                INSERT INTO CREDENTIALS (url, username, encodedpassword, iv, userid) 
                VALUES(#{url}, #{username}, #{encodedPassword}, #{iv}, #{userId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    public Integer addCredential(Credentials Credentials);


    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{id}")
    public void deletCredential(Integer id);

    @Update("""
            UPDATE CREDENTIALS 
            SET url = #{url}, username = #{username}, encodedpassword = #{encodedPassword}, iv=#{iv}
            WHERE credentialid = #{credentialId};
            """)
    public void updateCredential(Credentials Credentials);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{id}")
    public List<Credentials> findAllCredentials(Integer userid);
    
    @Select("SELECT iv FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    public String findCredential_Iv_ById(Integer credentialid);
}
