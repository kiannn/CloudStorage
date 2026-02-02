package com.jwdnd.cloudstorage.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Files {
    
    Integer fileId ; 
    String fileName ;
    String contentType ;
    String fileSize ;
    Integer userId ;
    
    // Stored as MEDIUMBLOB in DB 
    byte[] fileData ;
    
}
