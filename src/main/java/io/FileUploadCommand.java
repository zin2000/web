package io;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadCommand {
  private MultipartFile file = null;
  private MultipartFile file2 = null;
  private String name1 = null;
  private String name2 = null;
  
  public void setFile(MultipartFile file) {
    this.file = file;
  }
  public MultipartFile getFile() {
    return file;
  }
  
  public void setFile2(MultipartFile file2) {
	this.file2 = file2;
  }
  public MultipartFile getFile2() {
	return file2;
  }
  
  public void setName1(String name1) {
	this.name1 = name1;
  }
  public String getName1() {
	return name1;
  }
  
  public void setName2(String name2) {
	this.name2 = name2;
  }
  public String getName2() {
	return name2;
  }
  
}