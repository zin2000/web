/**
 * 
 */
package controller;

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
public class OldCoinMyDetailController extends BaseZwsController {

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
		view.addObject("page_info", "マイコレクション古銭(詳細)ページ");
		String idString = request.getParameter("id");
		int id;
		if(idString == null || idString.equals("")){
			id = 0;
		}else{
			try {
				id = Integer.parseInt(idString);
			} catch (Exception e) {
				id = 0;
			}
		}
		OldcoinDetail bean = service.queryMyDetailData(id);
		StringBuffer sb = new StringBuffer();
		sb.append("<br />");

		view.addObject("page_contents", (getItemListForTableString(bean))+sb.toString());
		return view;
	}
	

	/**
	 * @param itemList
	 */
	public String getItemListForTableString(OldcoinDetail item) {
		StringBuffer sb = new StringBuffer("<table class=\"table table-hover\" border=\"1\"><tr><td>ID</td><td>追加日</td><td>名前</td><td>画像表</td><td>画像裏</td><td>書体</td><td>素材</td><td>鋳造年</td></tr>");
		sb.append("<tr>");
		sb.append("<td>"+item.getDetailId()+"</td>");
		sb.append("<td>"+item.getAddDate()+"</td>");
		sb.append("<td>"+item.getName()+"</td>");
		sb.append("<td width=\"10%\">");
		if(item.getFrontImgUrl()!=null){
			sb.append("<a href=\""+item.getFrontImgUrl()+"\" >");
			sb.append("<img src=\""+item.getFrontImgUrl()+"\" class=\"img-responsive\" />");
			sb.append("</a>");
		}
		sb.append("</td>");
		sb.append("<td width=\"10%\">");
		if(item.getBackImgUrl()!=null){
			sb.append("<a href=\""+item.getBackImgUrl()+"\" >");
			sb.append("<img src=\""+item.getBackImgUrl()+"\" class=\"img-responsive\" />");
			sb.append("</a>");
		}
		sb.append("</td>");
		sb.append("<td>"+item.getFontName()+"</td>");
		sb.append("<td>"+item.getMaterialName()+"</td>");
		sb.append("<td>"+item.getStartYear()+"～"+item.getEndtYear()+"</td>");
		sb.append("</tr>");
		sb.append("</table>");
		
		sb.append("<div class=\"panel panel-success\">");
		 sb.append("<div class=\"panel-heading\">");
		  sb.append("<h3 class=\"panel-title\">"+item.getName()+"</h3>");
		 sb.append("</div>");
		 sb.append("<div class=\"panel-body\">");		
		  sb.append("<div class=\"row\">");
		  
		   sb.append("<div class=\"col-xs-6 col-md-3\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"ID"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
		      sb.append(item.getDetailId());
		     sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   
		   sb.append("<div class=\"col-xs-6 col-md-3\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"追加日"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
		      sb.append(item.getAddDate());
		     sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   
		   sb.append("<div class=\"col-xs-6 col-md-3\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"書体"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
		      sb.append(item.getFontName());
		     sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   
		   sb.append("<div class=\"col-xs-6 col-md-3\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"素材"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
		      sb.append(item.getMaterialName());
		     sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   
		   sb.append("<div class=\"col-xs-6 col-md-3\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"製造期間"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
		      sb.append(item.getStartYear()+"～"+item.getEndtYear());
		     sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   
		   sb.append("<div class=\"col-xs-12\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"画像表"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
				if(item.getFrontImgUrl()!=null){
					sb.append("<a href=\""+item.getFrontImgUrl()+"\" >");
					sb.append("<img src=\""+item.getFrontImgUrl()+"\" class=\"img-responsive\" />");
					sb.append("</a>");
				}
			 sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   
		   sb.append("<div class=\"col-xs-12\">");
		    sb.append("<div class=\"panel panel-success\">");
		     sb.append("<div class=\"panel-heading\">");
		      sb.append("<h3 class=\"panel-title\">"+"画像裏"+"</h3>");
		     sb.append("</div>");
		     sb.append("<div class=\"panel-body\">");
				if(item.getBackImgUrl()!=null){
					sb.append("<a href=\""+item.getBackImgUrl()+"\" >");
					sb.append("<img src=\""+item.getBackImgUrl()+"\" class=\"img-responsive\" />");
					sb.append("</a>");
				}
		     sb.append("</div>");
		    sb.append("</div>");
		   sb.append("</div>");
		   		   
		  sb.append("</div>");
		 sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
}
