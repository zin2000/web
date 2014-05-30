/**
 * 
 */
package controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import service.OldcoinService;

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
		//ApplicationContext context = getApplicationContext();
		ServletContext sv_context = request.getSession().getServletContext();
		WebApplicationContext context = WebApplicationContextUtils
		.getRequiredWebApplicationContext(sv_context);
        //new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (OldcoinService) context.getBean("oldcoinService");
		
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "マイコレクション古銭ページ");
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
		StringBuffer sb = new StringBuffer("<table class=\"table table-hover\" border=\"1\"><tr>");
		//sb.append("<td>ID</td><td>追加日</td>");
		sb.append("<td>名前</td><td>画像表</td><td>画像裏</td>");
		//sb.append("<td>書体</td><td>素材</td><td>鋳造年</td>");
		sb.append("<td>詳細</td>");
		sb.append("</tr>");
		for(OldcoinDetail item : itemList){
			sb.append("<tr>");
			//sb.append("<td>"+item.getDetailId()+"</td>");
			//sb.append("<td>"+item.getAddDate()+"</td>");
			sb.append("<td>"+item.getName()+"</td>");
			sb.append("<td>");
			if(item.getFrontImgUrl()!=null){
				sb.append("<a href=\""+item.getFrontImgUrl()+"\" >");
				sb.append("<img src=\""+item.getFrontImgUrl()+"\" class=\"img-responsive\" />");
				sb.append("</a>");
			}
			sb.append("</td>");
			sb.append("<td>");
			if(item.getBackImgUrl()!=null){
				sb.append("<a href=\""+item.getBackImgUrl()+"\" >");
				sb.append("<img src=\""+item.getBackImgUrl()+"\" class=\"img-responsive\" />");
				sb.append("</a>");
			}
			sb.append("</td>");
			sb.append("<td>");
			sb.append("<form method='post' action='oldcoindetail'>").
			append("<input type='hidden' name='id' value='"+item.getDetailId()+"'>").
			append("<input type=submit name='submit' value='詳細' class='btn'>").
			append("</form>");
			sb.append("</td>");
			//sb.append("<td>"+item.getFontName()+"</td>");
			//sb.append("<td>"+item.getMaterialName()+"</td>");
			//sb.append("<td>"+item.getStartYear()+"～"+item.getEndtYear()+"</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
}
