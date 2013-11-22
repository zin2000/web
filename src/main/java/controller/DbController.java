package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import service.IBumonService;
/**
 * @author jin-s
 *
 */
public class DbController extends BaseZwsController {
	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IBumonService service;
		ApplicationContext context = 
        new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (IBumonService) context.getBean("bumonService");
        
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "DBテストページ");
		view.addObject("page_contents", (String)service.getBumon("000001").getNmBumon());
		return view;
	}

}
