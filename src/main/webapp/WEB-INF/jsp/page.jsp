<%
{
	//画面表示に必要な項目を取得する
	java.lang.String pageInfo = (java.lang.String)request.getAttribute("page_info");
	java.lang.String pageContents = (java.lang.String)request.getAttribute("page_contents");
	java.lang.String footerInfo = (java.lang.String)request.getAttribute("footer_info");
	java.lang.String userAgent = (java.lang.String)request.getAttribute("user_agent");
	java.lang.String userAgentName = "";
	java.lang.Boolean isMobile = false;
	java.lang.String[] userAgentNameMobileList = {"iPhone","iPod","Android","Windows Phone","BlackBerry"};
	java.lang.String[] userAgentNameGameList = {"Wii","GameBoy","Nitro","PSP", "PS2","PLAYSTATION 3","3DS"};
	java.lang.String[] userAgentNameList = {"MSIE","Chrome","Lunascape","Netscape","Firefox","Safari","Opera"};
%>
<html>
<head>
<%
if(userAgentName.equals("")){
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
if(isMobile){
%>
<jsp:include page="/WEB-INF/jsp/menuMobile_h.jsp" flush="true" />
<%
}else{
%>
<jsp:include page="/WEB-INF/jsp/menu_h.jsp" flush="true" />
<%
}
%>
</head>
  <body bgcolor="white">
    <div data-role="page">
<%
if(isMobile){
%>
<jsp:include page="/WEB-INF/jsp/menuMobile.jsp" flush="true" />
<%
}else{
%>
<jsp:include page="/WEB-INF/jsp/menu.jsp" flush="true" />
<%
}
%>
      <div data-role="content">
      <p><%=pageInfo%></p>
      <p><%=pageContents%></p>
      </div>
<%
if(isMobile){
%>
<footer data-role="footer">
<h1><%="your access from "%><%=userAgentName%><%=" "%><%=footerInfo%></h1>
</footer><%
}else{
%>
<div id="globalfooter">
<div><%="your access from "%><%=userAgentName%><%=" "%><%=footerInfo%></div>
</div>
<%
}
%>


    </div>
  </body>
</html>
<%
}
%>
