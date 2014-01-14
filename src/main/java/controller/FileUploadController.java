package controller;
import io.FileUploadCommand;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
@SuppressWarnings("deprecation")
public class FileUploadController extends SimpleFormController {

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request,
    HttpServletResponse response, Object command,
    BindException bind) throws Exception {

    FileUploadCommand fileUploadCommnad = 
      (FileUploadCommand) command;
    MultipartFile file = fileUploadCommnad.getFile();
    MultipartFile file2 = fileUploadCommnad.getFile2();
    String name1 = fileUploadCommnad.getName1();
    String name2 = fileUploadCommnad.getName2();
    if (!file.isEmpty()){
      //file.transferTo(new File("D:/tmp/アップロード.txt"));
      //file.transferTo(new File("../webapps/zins-work-shop/resources/img/アップロード.txt"));
    	String time = String.valueOf(new Date().getTime());
    	
        try {
            final String base64 =new String(Base64.encodeBase64(file.getBytes()));
        } catch (Exception e){
        	
        }
    	
    	file.getBytes();
    	file.transferTo(new File("/usr/share/tomcat6/webapps/zins-work-shop/resources/img/アップロード_"+name1+time+".txt"));
    	file2.transferTo(new File("/usr/share/tomcat6/webapps/zins-work-shop/resources/img/アップロード_"+name2+time+".txt"));
    	request.setAttribute("name1", request.getParameter("name1"));
    	request.setAttribute("name2", request.getParameter("name2"));
    	request.setAttribute("type", request.getParameter("submit"));
    }
    request.setAttribute("upload1", "");
    new ModelAndView(getSuccessView());
    return super.onSubmit(request, response, command, bind);
  }
}