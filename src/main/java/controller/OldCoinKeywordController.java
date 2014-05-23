/**
 * 
 */
package controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import service.DonjonItemService;
import service.OldcoinService;

import dao.jdbc.OldcoinDaoImpl;
import dto.DonjonEquItem;
import dto.OldcoinCharacter;
import dto.OldcoinDetail;
import dto.OldcoinKeyword;
import dto.OldcoinMaster;
/**
 * @author jin-s
 *
 */
public class OldCoinKeywordController extends BaseZwsController {

	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OldcoinService service;
		ApplicationContext context = getApplicationContext();
        //new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (OldcoinService) context.getBean("oldcoinService");
		
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "古銭情報検索ページ");
		String kubun = request.getParameter("kubun");
		String keyName = request.getParameter("word");
		List<OldcoinKeyword> key = null;

		StringBuffer sb = new StringBuffer();
		
		sb.append("<br />");
		sb.append("<div class=\"panel panel-success\"><div class=\"panel-heading\"><h3 class=\"panel-title\">穴銭(渡来銭)検索方法</h3></div><div class=\"panel-body\">不明な名称は[？](ワイルドカード)を選択して検索します<br />※例：<br />選択：[？][？][通][寶]<br />結果：○○通寶<br /><br />存在しない組み合わせの場合は結果は出力されません<br /></div></div>");
		
		if(keyName!=null && kubun.equals("0")){
			key = service.findCoinKeywordName(keyName);
		}else if(keyName!=null && kubun.equals("1")){
			key = service.findCoinKeywordNote(keyName);
		}
		
		sb.append(getItemListForString(key));
		
		view.addObject("page_contents", getFormString(keyName)+sb.toString());
		return view;
	}
	

	/**
	 * @param itemList
	 */
	public String getItemListForString(List<OldcoinKeyword> itemList) {
		StringBuffer sb = new StringBuffer("<table class=\"table table-hover\" border='1'><tr><td>名前</td><td>よみがな</td><td>内容</td></tr>");
		for(OldcoinKeyword item : itemList){
			sb.append("<tr>");
			sb.append("<td>"+item.getKeyName()+"</td>");
			sb.append("<td>"+item.getKeyKana()+"</td>");
			sb.append("<td>"+item.getKeyNote()+"</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	/**
	 * 
	 */
	public String getFormString(String keyName) {
		StringBuffer sb = new StringBuffer("");
		sb.append("<FORM class=\"form-inline\" role=\"form\" method='post' name='change' action='oldcoinkeyword'>");
		sb.append("<INPUT type=\"text\" class=\"input-small\" name='word' value=\""+keyName+"\">");
		sb.append("<SELECT class=\"input-small\" name='kubun'>");
		sb.append("<option value='0'>名称検索");
		sb.append("<option value='1'>本文検索");
		sb.append("</SELECT>");
		sb.append("<button type=\"submit\" class=\"btn btn-default\">Submit</button>");
		sb.append("</FORM>");
		return sb.toString();
	}

	/**
	 * @param itemList
	 */
	public String getItemListForTableString(List<OldcoinDetail> itemList) {
		StringBuffer sb = new StringBuffer("<table class=\"table table-hover\" border=\"1\"><tr><td>ID</td><td>追加日</td><td>名前</td><td>画像表</td><td>画像裏</td><td>書体</td><td>素材</td><td>鋳造年</td></tr>");
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
