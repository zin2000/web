/**
 * 
 */
package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
/**
 * @author jin-s
 *
 */
public class MainController extends BaseZwsController {

	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = getMainModelAndView(request, response);
		view.addObject("page_info", "mainページ");
		view.addObject("page_contents", "<a href='/portal/main/zins-work-shop/top'>zin's work shop TOPへ</a>");
		return view;
	}

}
