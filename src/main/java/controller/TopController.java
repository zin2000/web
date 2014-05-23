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
public class TopController extends BaseZwsController {

	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "TOPページ");
		view.addObject("page_contents", "管理人ことzinの趣味のサイトです。<br />穴銭（渡来銭）の検索や、マイコレクションの紹介が現在のコンテンツです（今後拡充予定^^；）");
		return view;
	}

}
