/**
 * 
 */
package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import service.OldcoinService;

import dto.OldcoinCharacter;
import dto.OldcoinMaster;
/**
 * @author jin-s
 *
 */
public class OldCoinMasterController extends BaseZwsController {

	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OldcoinService service;
		ApplicationContext context = getApplicationContext();
        //new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (OldcoinService) context.getBean("oldcoinService");
		
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "古銭マスタページ");
		String c1 = request.getParameter("c1");
		String c2 = request.getParameter("c2");
		String c3 = request.getParameter("c3");
		String c4 = request.getParameter("c4");
		String[] cs = new String[]{"%","%","%","%"};
		if(c1!=null){cs[0]=c1;}
		if(c2!=null){cs[1]=c2;}
		if(c3!=null){cs[2]=c3;}
		if(c4!=null){cs[3]=c4;}
		//if(c1.equals("")){
		//	c1 = "%";
		//}
		//if(c2.equals("")){
		//	c2 = "%";
		//}
		//if(c3.equals("")){
		//	c3 = "%";
		//}
		//if(c4.equals("")){
		//	c4 = "%";
		//}
		int maxPage = service.queryCountDetailAll();
		List<OldcoinMaster> beanList = service.queryMasterData(c1, c2, c3, c4);
		List<OldcoinCharacter> select1 = service.findCoinCharacter1();
		List<OldcoinCharacter> select2 = service.findCoinCharacter2();
		List<OldcoinCharacter> select3 = service.findCoinCharacter3();
		List<OldcoinCharacter> select4 = service.findCoinCharacter4();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<br />");
		if(c1!=null && c2!=null && c3!=null && c4!=null){
			sb.append(getItemListForTableString(beanList));
			//view.addObject("page_contents", (getItemListForTableString(beanList))+sb.toString());
		}
		view.addObject("page_contents", (getFormString(select1,select2,select3,select4,cs))+sb.toString());
		return view;
	}
	

	/**
	 * @param itemList
	 */
	public String getItemListForTableString(List<OldcoinMaster> itemList) {
		StringBuffer sb = new StringBuffer("<table class=\"table table-hover\" border='1'><tr><td>ID</td><td>名前</td><td>鋳造年</td><td>備考</td></tr>");
		for(OldcoinMaster item : itemList){
			sb.append("<tr>");
			sb.append("<td>"+item.getMasterId()+"</td>");
			sb.append("<td>"+item.getName()+"</td>");
			sb.append("<td>"+item.getStartYear()+"～"+item.getEndtYear()+"</td>");
			sb.append("<td>"+item.getNote()+"</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	
	/**
	 * 
	 */
	public String getFormString(List<OldcoinCharacter> select1,List<OldcoinCharacter> select2,List<OldcoinCharacter> select3,List<OldcoinCharacter> select4,String[] cs) {
		StringBuffer sb = new StringBuffer("");
		sb.append("<FORM class=\"form-inline\" role=\"form\" method='post' name='change' action='oldcoinmaster'>");
		sb.append("<SELECT class=\"input-small\" name='c1'>");
		if(cs[0] == "%"){
			sb.append("<option selected value='%'>？");
		}else{
			sb.append("<option value='%'>？");
		}
		for(OldcoinCharacter item : select1){
			sb.append("<option ");
			if(String.valueOf(item.getCharacterId()).equals(cs[0])){ sb.append("selected "); }
			sb.append("value='"+item.getCharacterId()+"'>"+item.getCharacterName());
		}
		sb.append("</SELECT>");
		sb.append("<SELECT class=\"input-small\" name='c2'>");
		if(cs[1] == "%"){
			sb.append("<option selected value='%'>？");
		}else{
			sb.append("<option value='%'>？");
		}
		for(OldcoinCharacter item : select2){
			sb.append("<option ");
			if(String.valueOf(item.getCharacterId()).equals(cs[1])){ sb.append("selected "); }
			sb.append("value='"+item.getCharacterId()+"'>"+item.getCharacterName());
		}
		sb.append("</SELECT>");
		sb.append("<SELECT class=\"input-small\" name='c3'>");
		if(cs[2] == "%"){
			sb.append("<option selected value='%'>？");
		}else{
			sb.append("<option value='%'>？");
		}
		for(OldcoinCharacter item : select3){
			sb.append("<option ");
			if(String.valueOf(item.getCharacterId()).equals(cs[2])){ sb.append("selected "); }
			sb.append("value='"+item.getCharacterId()+"'>"+item.getCharacterName());
		}
		sb.append("</SELECT>");
		sb.append("<SELECT class=\"input-small\" name='c4'>");
		if(cs[3] == "%"){
			sb.append("<option selected value='%'>？");
		}else{
			sb.append("<option value='%'>？");
		}
		for(OldcoinCharacter item : select4){
			sb.append("<option ");
			if(String.valueOf(item.getCharacterId()).equals(cs[3])){ sb.append("selected "); }
			sb.append("value='"+item.getCharacterId()+"'>"+item.getCharacterName());
		}
		sb.append("</SELECT>");
		sb.append("<button type=\"submit\" class=\"btn btn-default\">Submit</button>");
		sb.append("</FORM>");
		return sb.toString();
	}
}
