package com.jwdnd.cloudstorage.services;

import com.jwdnd.cloudstorage.Mappers.FileMapper;
import com.jwdnd.cloudstorage.Mappers.UserMapper;
import com.jwdnd.cloudstorage.Model.Files;
import com.jwdnd.cloudstorage.Model.User;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    private FileMapper FileMapper;
    
    @Autowired
    private UserMapper UserMapper;
    
    @Autowired
    CompressionService CompressionService;

    public List<Files> getAllFiles(String username) {
        
        User user = UserMapper.getUserByUserName(username);
        List<Files> allFiles = FileMapper.getAllFiles(user.getUserid());

        return allFiles;
        
    }
    
   public Files deleteFile(Integer fileId) {
       
       Files deletedFile = FileMapper.viewFile(fileId);

       FileMapper.deleteFile(fileId); 
       
       return deletedFile;
        
    }
   
   public void addFile(MultipartFile fileUpload, String name) throws IOException {

        User userByUserName = UserMapper.getUserByUserName(name);
        byte[] bytes = fileUpload.getBytes();
        
        // Do not compress the file if it's of a zip file
        bytes = fileUpload.getOriginalFilename().endsWith(".zip") ? bytes : CompressionService.compress(bytes);
        
        Files build = Files.builder()
                           .fileName(fileUpload.getOriginalFilename())
                           .fileSize(fileUpload.getSize()+" KB")
                           .userId(userByUserName.getUserid())
                           .fileData(bytes)
                           .contentType(fileUpload.getContentType())
                           .build();
                            
        FileMapper.addFile(build);
       
        
    }
   
   public Files viewFile(Integer fileId) {
       
        Files viewFile = FileMapper.viewFile(fileId);
        byte[] fileData = viewFile.getFileData();
        
        fileData = viewFile.getFileName().endsWith(".zip") ? fileData : CompressionService.decompress(fileData) ;
        
        viewFile.setFileData(fileData);
        
        return viewFile;
        
    }

   public boolean isDuplicate(MultipartFile fileUpload, String name){

        User userByUserName = UserMapper.getUserByUserName(name);
        List<String> allFiles = FileMapper.getAllFilesNames(userByUserName.getUserid());
        
        boolean contains = allFiles.contains(fileUpload.getOriginalFilename());
        
        return contains;
        
   }
    public boolean isValidFileId(String username, Integer fileId) {
        
        List<Files> allFiles = getAllFiles(username);

        List<Integer> fileIds = allFiles.stream()
                                        .map(file -> file.getFileId())
                                        .collect(Collectors.toList());
        
        boolean contains = fileIds.contains(fileId);
        
        return contains;

    }

}
