package controller;
import io.FileUploadCommand;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
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
    if (!file.isEmpty()){
      //file.transferTo(new File("D:/tmp/アップロード.txt"));
      file.transferTo(new File("/アップロード.txt"));
    }
    return super.onSubmit(request, response, command, bind);
  }
}