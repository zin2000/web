<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/xml" %>
<%{
	//画面表示に必要な項目を取得する
	java.lang.String pageInfo = (java.lang.String)request.getAttribute("page_info");
	java.lang.String pageContents = (java.lang.String)request.getAttribute("page_contents");
	java.lang.String userAgent = (java.lang.String)request.getAttribute("user_agent");
	java.lang.String userAgentName = "";
	java.lang.Boolean isMobile = false;
	java.lang.String[] userAgentNameMobileList = {"iPhone","iPod","Android","Windows Phone","BlackBerry"};
	java.lang.String[] userAgentNameGameList = {"Wii","GameBoy","Nitro","PSP", "PS2","PLAYSTATION 3","3DS"};
	java.lang.String[] userAgentNameList = {"MSIE","Chrome","Lunascape","Netscape","Firefox","Safari","Opera"};%>
<%if(userAgentName.equals("")){
  for(String userAgentIs : userAgentNameMobileList){
    if(userAgent.indexOf(userAgentIs) > -1){
	  isMobile = true;
	  userAgentName = userAgentIs;
	  break;
    }
  }
}
if(userAgentName.equals("")){
  for(String userAgentIs : userAgentNameGameList){
	  if(userAgent.indexOf(userAgentIs) > -1){
		isMobile = true;
		userAgentName = userAgentIs;
		break;
	  }
  }
}
if(userAgentName.equals("")){
  for(String userAgentIs : userAgentNameList){
	  if(userAgent.indexOf(userAgentIs) > -1){
		userAgentName = userAgentIs;
		break;
	  }
  }
}
if(userAgentName.equals("")){
	userAgentName = "Other?";
}
%>
<%=pageContents%>
<%}%>
