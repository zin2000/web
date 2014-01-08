/**
 * 
 */
package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import service.DonjonItemService;
import service.OldcoinService;

import dao.jdbc.OldcoinDaoImpl;
import dto.DonjonEquItem;
import dto.OldcoinDetail;
/**
 * @author jin-s
 *
 */
public class OldCoinController extends BaseZwsController {

	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OldcoinService service;
		ApplicationContext context = 
        new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (OldcoinService) context.getBean("oldcoinService");
		
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "古銭ページ");
		String pageString = request.getParameter("page");
		int page;
		if(pageString == null || pageString.equals("")){
			page = 0;
		}else{
			try {
				page = Integer.parseInt(pageString);
			} catch (Exception e) {
				page = 0;
			}
		}
		int maxPage = service.queryCountDetailAll();
		List<OldcoinDetail> beanList = service.queryPageDetailData(page);
		StringBuffer sb = new StringBuffer();
		sb.append("<br />");
		if(page >= 10){
			//sb.append("<form method='post' action='oldcoin'>").
			//append("<input type='hidden' name='page' value='"+(page-10)+"'>").
			//append("<input type=submit name='submit' value='←back'>").
			//append("</form>");
			
			sb.append("<form name='back' method='post' action='oldcoin'>").
			append("<input type='hidden' name='page' value='"+(page-10)+"'>").
			append("</form>").
			append("<a href='javascript:back.submit()'>Back</a>").
			append("<img src=\"/zins-work-shop/resources/img/sp.png\" width=\"20\" />");
		}
		
		if(page+10 < maxPage){
			//sb.append("<form method='post' action='oldcoin'>").
			//append("<input type='hidden' name='page' value='"+(page+10)+"'>").
			//append("<input type=submit name='submit' value='next→'>").
			//append("</form>");
			
			sb.append("<a href='javascript:next.submit()'>Next</a>").
			append("<form name='next' method='post' action='oldcoin'>").
			append("<input type='hidden' name='page' value='"+(page+10)+"'>").
			append("</form>");
		}
		view.addObject("page_contents", (getItemListForTableString(beanList))+sb.toString());
		return view;
	}
	

	/**
	 * @param itemList
	 */
	public String getItemListForTableString(List<OldcoinDetail> itemList) {
		StringBuffer sb = new StringBuffer("<table border='1'><tr><td>ID</td><td>追加日</td><td>名前</td><td>画像表</td><td>画像裏</td><td>書体</td><td>素材</td><td>鋳造年</td></tr>");
		for(OldcoinDetail item : itemList){
			sb.append("<tr>");
			sb.append("<td>"+item.getDetailId()+"</td>");
			sb.append("<td>"+item.getAddDate()+"</td>");
			sb.append("<td>"+item.getName()+"</td>");
			sb.append("<td><img src='"+item.getFrontImgUrl()+"' /></td>");
			sb.append("<td><img src='"+item.getBackImgUrl()+"' /></td>");
			sb.append("<td>"+item.getFontName()+"</td>");
			sb.append("<td>"+item.getMaterialName()+"</td>");
			sb.append("<td>"+item.getStartYear()+"～"+item.getEndtYear()+"</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
}
