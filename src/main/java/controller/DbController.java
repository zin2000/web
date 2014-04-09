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
import service.OldcoinService;
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
		ApplicationContext context = getApplicationContext();
        //new ClassPathXmlApplicationContext("applicationContext.xml");
        service = (DonjonItemService) context.getBean("donjonItemService");
        String modeString = request.getParameter("mode");
        String versionString = request.getParameter("ver");
		
		List<DonjonEquItem> itemList = new ArrayList<DonjonEquItem>();
		ModelAndView view = null;
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
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(itemList)));
				break;
			case 2:
				itemList = service.queryEquItemVersion(Integer.parseInt(versionString));
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(itemList)));
				break;
			case 3:
				itemList = service.queryEquItemVersionTo(Integer.parseInt(versionString));
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(itemList)));
				break;
			case 4:
				itemList = service.queryItemAll();
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(itemList)));
				break;
			case 5:
				itemList = service.queryItemVersion(Integer.parseInt(versionString));
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(itemList)));
				break;
			case 6:
				itemList = service.queryItemVersionTo(Integer.parseInt(versionString));
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(itemList)));
				break;
			case 7:
				int dbv = service.queryItemMaxVersion();
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(dbv)));
				break;
			case 8:
				int dbv_e = service.queryEquItemMaxVersion();
				view = getXmlModelAndView(request, response);
				view.addObject("page_contents", (getItemListForXmlString(dbv_e)));
				break;
			default:
				itemList = service.queryEquItemVersion(0);
				view = getDefaultModelAndView(request, response);
				view.addObject("page_info", "DBテストページ");
				view.addObject("page_contents", (getItemListForTableString(itemList)));
				break;
			}	
		}
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
	
	/**
	 * @param itemList
	 */
	public String getItemListForXmlString(List<DonjonEquItem> itemList) {
		StringBuffer sb = new StringBuffer("<data>");
		for(DonjonEquItem item : itemList){
			sb.append("<row>");
			sb.append("<item_detail_id>"+String.format("%1$08d", item.getItemDetailId())+"</item_detail_id>");
			sb.append("<item_type_id>"+item.getItemTypeId()+"</item_type_id>");
			sb.append("<item_img_id>"+item.getItemImgId()+"</item_img_id>");
			sb.append("<item_name>"+item.getItemName()+"</item_name>");
			sb.append("<point>"+item.getPoint()+"</point>");
			sb.append("<skill_id>"+item.getSkillId()+"</skill_id>");
			if(item.isEquFlag()){
				sb.append("<equ_flag>1</equ_flag>");
			}else{
				sb.append("<equ_flag>0</equ_flag>");
			}
			sb.append("<use_count>"+item.getUseCount()+"</use_count>");
			sb.append("<item_type_name>"+item.getItemTypeName()+"</item_type_name>");
			sb.append("<item_img_binary>"+item.getItemImgBinary()+"</item_img_binary>");
			sb.append("<item_img_mime>"+item.getItemImgMime()+"</item_img_mime>");
			sb.append("<item_version>"+item.getItemVersion()+"</item_version>");
			sb.append("</row>");
		}
		sb.append("</data>");
		return sb.toString();
	}
	
	/**
	 * @param itemList
	 */
	public String getItemListForXmlString(int itemVersion) {
		StringBuffer sb = new StringBuffer("<data>");
		sb.append("<row>");
		sb.append("<item_version>"+itemVersion+"</item_version>");
		sb.append("</row>");
		sb.append("</data>");
		return sb.toString();
	}
}
