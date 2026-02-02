
package com.jwdnd.cloudstorage.Mappers;

import com.jwdnd.cloudstorage.Model.FileDownload;
import com.jwdnd.cloudstorage.Model.FileMetadata;
import com.jwdnd.cloudstorage.Model.Files;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {

    @Insert("""
            INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) 
            VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    public Integer addFile(Files Files);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    public void deleteFile(Integer fileId);
    
    @Select("SELECT fileId, filename, filesize FROM FILES WHERE userid = #{userid}")
    public List<FileMetadata> getAllFiles(Integer userid);
    
    @Select("SELECT filename FROM FILES WHERE userid = #{userid}")
    public List<String> getAllFilesNames(Integer userid);
    
    @Select("SELECT filedata, contenttype, filename FROM FILES WHERE fileId = #{fileId}")
    public FileDownload viewFile(Integer fileId);
    
    @Select("SELECT filename FROM FILES WHERE fileId = #{fileId}")
    public String getFileName(Integer fileId);
}
