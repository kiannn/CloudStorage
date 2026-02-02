package com.jwdnd.cloudstorage.controllers;

import com.jwdnd.cloudstorage.Model.FileDownload;
import com.jwdnd.cloudstorage.Model.Files;
import com.jwdnd.cloudstorage.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
@SessionAttributes({"msg", "authorizedUser"})
public class FileController {

    @Autowired
    FileService FileService;
    
    @Autowired
    HomeController HomeController;

    @PostMapping("uploadfile")
    public String uploadFile(MultipartFile fileUpl, ModelMap ModelMap, @Value("${base.url}") String base) throws IOException {
 
        String msg = "";

        if (!fileUpl.isEmpty()) {

            String name = (String) ModelMap.getAttribute("authorizedUser");
            boolean duplicate = FileService.isDuplicate(fileUpl, name);

            msg = fileUpl.getOriginalFilename();
            msg = msg.length() < 20 ? msg : msg.substring(0, 20) + "...";
            
            if (!duplicate) {

                FileService.addFile(fileUpl, name);
            }
           
            msg = !duplicate ? "File name  " + msg + " uploaded successfully !"
                             : "File name  " + msg + " is duplicate";
        }

        ModelMap.addAttribute("msg", msg);
        HomeController.showMsg = 'y';
        
        return "redirect:"+base+"home";

    }

    @GetMapping("viewfile/{fileId}")
    @ResponseBody
    public ResponseEntity<byte[]> viewFile(@PathVariable Integer fileId, ModelMap ModelMap, HttpServletRequest r) throws AccessDeniedException {

        String name = (String) ModelMap.getAttribute("authorizedUser"); 
        
        boolean validFileId = FileService.isValidFileId(name, fileId);

        boolean reject = r.getHeader("Referer")==null || !r.getHeader("Referer").contains("home");
        
        if (validFileId && !reject) {
            
            FileDownload viewFile = FileService.viewFile(fileId);
            ResponseEntity<byte[]> response = ResponseEntity.status(HttpStatus.OK)
                                                            .contentType(MediaType.valueOf(viewFile.getContentType()))
                                                            .body(viewFile.getFileData());
            return response;
        }
          HomeController.showMsg = 'y';
          
          throw new AccessDeniedException(null);
    }

    @GetMapping("deletefile/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, ModelMap ModelMap, HttpServletRequest r, @Value("${base.url}")String base) {
      
        String name = (String) ModelMap.getAttribute("authorizedUser");

        boolean validFileId = FileService.isValidFileId(name, fileId);

        boolean reject = r.getHeader("Referer") == null || !r.getHeader("Referer").contains("home");

        if (validFileId && !reject) {
            String msg;
            msg = FileService.deleteFile(fileId);
            msg = msg.length() < 20 ? msg : msg.substring(0, 20) + "...";
            msg = "File name  " + msg + "  deleted successfully !";

            ModelMap.addAttribute("msg", msg);
            
        }
        HomeController.showMsg = 'y';
        
        return "redirect:" + base + "home";

    }

}
