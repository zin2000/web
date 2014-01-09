package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
        String modeString = request.getParameter("mode");
        String versionString = request.getParameter("ver");
        
		ModelAndView view = getDefaultModelAndView(request, response);
		view.addObject("page_info", "DBテストページ");
		
		List<DonjonEquItem> itemList = new ArrayList<DonjonEquItem>();
		
		try {
			Integer.parseInt(modeString);
			Integer.parseInt(versionString);
		} catch (Exception e) {
			modeString=null;
			versionString=null;
		}
		
		if(modeString!=null && versionString!=null){
			switch (Integer.parseInt(modeString)) {
			case 1:
				itemList = service.queryEquItemAll();
				break;
			case 2:
				itemList = service.queryEquItemVersion(Integer.parseInt(versionString));
				break;
			case 3:
				itemList = service.queryEquItemVersionTo(Integer.parseInt(versionString));
				break;
			case 4:
				itemList = service.queryItemAll();
				break;
			case 5:
				itemList = service.queryItemVersion(Integer.parseInt(versionString));
				break;
			case 6:
				itemList = service.queryItemVersionTo(Integer.parseInt(versionString));
				break;
			default:
				itemList = service.queryEquItemVersion(0);
				break;
			}	
		}
		view.addObject("page_contents", (getItemListForTableString(itemList)));
		return view;
	}
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerInput(Model model) {
        model.addAttribute("");
        return "register/input";
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
