
package com.jwdnd.cloudstorage.Mappers;

import com.jwdnd.cloudstorage.Model.Notes;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NotesMapper {

    @Insert("""
            INSERT INTO NOTES (notetitle, notedescription, userid) 
            VALUES(#{noteTitle}, #{noteDescription}, #{userId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    public void addNote(Notes Notes);

    @Select("SELECT * FROM NOTES WHERE userid = #{id}")
    public List<Notes> getAllNotes(Integer id);

    @Delete("DELETE FROM NOTES WHERE noteid = #{id}")
    public void deletNote(Integer id);

    @Select("SELECT * FROM NOTES WHERE noteid = #{id}")
    public Notes getNote(Integer id);
    
    @Update("""
            UPDATE NOTES
            SET notetitle = #{noteTitle}, notedescription = #{noteDescription}
            WHERE noteid = #{noteId};
            """)
    public void updateNote(Notes Notes);
    
}
