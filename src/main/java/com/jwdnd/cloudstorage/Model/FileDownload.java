package com.jwdnd.cloudstorage.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDownload {

    byte[] fileData;
    String filename;
    String contentType;

}
