<%
{
	//画面表示に必要な項目を取得する
	java.lang.String pageInfo = (java.lang.String)request.getAttribute("page_info");
	java.lang.String footerInfo = (java.lang.String)request.getAttribute("footer_info");
	java.lang.String userAgent = (java.lang.String)request.getAttribute("user_agent");
	java.lang.String userAgentName = "";
	java.lang.Boolean isMobile = false;
%>
<html>
<head>
<%
if(userAgent.indexOf("iPhone") > -1){
	isMobile = true;
	userAgentName = "iPhone";
}else if(userAgent.indexOf("iPod") > -1){
	isMobile = true;
	userAgentName = "iPod";
}else if(userAgent.indexOf("iPad") > -1){
	isMobile = true;
	userAgentName = "iPad";
}else if(userAgent.indexOf("Android") > -1){
	isMobile = true;
	userAgentName = "Android";
}else if(userAgent.indexOf("Windows Phone") > -1){
	isMobile = true;
	userAgentName = "Windows Phone";
}else if(userAgent.indexOf("BlackBerry") > -1){
	isMobile = true;
	userAgentName = "BlackBerry";
}else if(userAgent.indexOf("Wii") > -1){
	isMobile = true;
	userAgentName = "Wii";
}else if(userAgent.indexOf("GameBoy") > -1){
	isMobile = true;
	userAgentName = "GameBoy";
}else if(userAgent.indexOf("Nitro") > -1){
	isMobile = true;
	userAgentName = "Nitro";
}else if(userAgent.indexOf("PSP") > -1){
	isMobile = true;
	userAgentName = "PSP";
}else if(userAgent.indexOf("PS2") > -1){
	isMobile = true;
	userAgentName = "PS2";
}else if(userAgent.indexOf("PLAYSTATION 3") > -1){
	isMobile = true;
	userAgentName = "PLAYSTATION 3";
}else if(userAgent.indexOf("MSIE") > -1){
	userAgentName = "MSIE";
}else if(userAgent.indexOf("Chrome") > -1){
	userAgentName = "Chrome";
}else if(userAgent.indexOf("Lunascape") > -1){
	userAgentName = "Lunascape";
}else if(userAgent.indexOf("Netscape") > -1){
	userAgentName = "Netscape";
}else if(userAgent.indexOf("Firefox") > -1){
	userAgentName = "Firefox";
}else if(userAgent.indexOf("Safari") > -1){
	userAgentName = "Safari";
}else if(userAgent.indexOf("Opera") > -1){
	userAgentName = "Opera";
}else{
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
      </div>
      <footer data-role="footer">
        <h1><%="your access from "%><%=userAgentName%><%=" "%><%=footerInfo%></h1>
      </footer>
    </div>
  </body>
</html>
<%
}
%>