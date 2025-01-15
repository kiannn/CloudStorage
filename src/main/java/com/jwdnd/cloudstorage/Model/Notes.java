package com.jwdnd.cloudstorage.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Notes {

    Integer noteId; 
    String noteTitle; 
    String noteDescription;
    Integer userId;

    @Override
    public String toString() {
        return "Notes{" + "noteId=" + noteId + ", noteTitle=" + noteTitle + ", noteDescription=" + noteDescription + ", userId=" + userId + '}';
    }

}
