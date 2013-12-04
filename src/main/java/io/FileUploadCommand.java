package io;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadCommand {
  private MultipartFile file = null;
  public void setFile(MultipartFile file) {
    this.file = file;
  }
  public MultipartFile getFile() {
    return file;
  }
}