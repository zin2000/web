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
public class GameController extends BaseZwsController {

	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "ゲーム開発ページ");
		return view;
	}

}
