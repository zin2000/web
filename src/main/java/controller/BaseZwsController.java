package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public abstract class BaseZwsController implements Controller {

	public abstract ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public ModelAndView getDefaultModelAndView(HttpServletRequest request, HttpServletResponse response){
		//Class名の取得
		String className = this.getClass().getSimpleName().split("Controller")[0];
		className = className.toLowerCase();
		//リクエスト情報のひな形の作成
		ModelAndView view = new ModelAndView("page");
		view.addObject("footer_info", "Copyright (c) 2013 zins work shop. All Rights Reserved <a href='/index.html'>土竜庵TOPへ</a>");
		view.addObject("user_agent", request.getHeader("User-Agent"));
		return view;
	}
}