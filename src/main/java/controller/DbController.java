package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import dto.DonjonEquItem;

import service.DonjonItemService;
/**
 * @author jin-s
 *
 */
public class DbController extends BaseZwsController {
	/* 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DonjonItemService service;
		ApplicationContext context = 
        new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (DonjonItemService) context.getBean("donjonItemService");
        
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "DBテストページ");
		List<DonjonEquItem> itemList = service.queryEquItemVersion(1);
		
		view.addObject("page_contents", (getItemListForTableString(itemList)));
		return view;
	}

	/**
	 * @param itemList
	 */
	public String getItemListForTableString(List<DonjonEquItem> itemList) {
		StringBuffer sb = new StringBuffer("<table border='1'><tr><td>ID</td><td>タイプID</td><td>画像ID</td><td>名称</td><td>ポイント</td><td>スキルID</td><td>装備フラグ</td><td>使用数</td><td>Ver</td><td>タイプ名</td><td>画像</td></tr>");
		for(DonjonEquItem item : itemList){
			sb.append("<tr>");
			sb.append("<td>"+item.getItemDetailId()+"</td>");
			sb.append("<td>"+item.getItemTypeId()+"</td>");
			sb.append("<td>"+item.getItemImgId()+"</td>");
			sb.append("<td>"+item.getItemName()+"</td>");
			sb.append("<td>"+item.getPoint()+"</td>");
			sb.append("<td>"+item.getSkillId()+"</td>");
			sb.append("<td>"+item.isEquFlag()+"</td>");
			sb.append("<td>"+item.getUseCount()+"</td>");
			sb.append("<td>"+item.getItemVersion()+"</td>");
			sb.append("<td>"+item.getItemTypeName()+"</td>");
			sb.append("<td><img src=data:"+item.getItemImgMime()+";base64,"+item.getItemImgBinary()+" /></td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

}
