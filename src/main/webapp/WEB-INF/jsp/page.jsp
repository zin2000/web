<!DOCTYPE html>
<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" isELIgnored="false"%>
<%{
	//画面表示に必要な項目を取得する
	java.lang.String pageInfo = (java.lang.String)request.getAttribute("page_info");
	java.lang.String pageContents = (java.lang.String)request.getAttribute("page_contents");
	java.lang.String footerInfo = (java.lang.String)request.getAttribute("footer_info");
	java.lang.String userAgent = (java.lang.String)request.getAttribute("user_agent");
	java.lang.String userAgentName = "";
	java.lang.Boolean isMobile = false;
	java.lang.String[] userAgentNameMobileList = {"iPhone","iPod","Android","Windows Phone","BlackBerry"};
	java.lang.String[] userAgentNameGameList = {"Wii","GameBoy","Nitro","PSP", "PS2","PLAYSTATION 3","3DS"};
	java.lang.String[] userAgentNameList = {"MSIE","Chrome","Lunascape","Netscape","Firefox","Safari","Opera"};%>
<html>
<head>
<title>zin's work shop - <%=pageInfo%> -</title>
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
if(isMobile){%>
<jsp:include page="/WEB-INF/jsp/menu_h_m.jsp" flush="true" />
<%}else{%>
<jsp:include page="/WEB-INF/jsp/menu_h.jsp" flush="true" />
<%}%>
</head>
<body bgcolor="white">
<div id="wrap">
<%if(isMobile){%>
<jsp:include page="/WEB-INF/jsp/menuMobile.jsp" flush="true" />
<%}else{%>
<jsp:include page="/WEB-INF/jsp/menuMobile.jsp" flush="true" />
<%}%>
	<div class="container">

	<div id="well">
<section class="visible-xs text-center adsense_1">
<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<!-- モバイル5 -->
<ins class="adsbygoogle"
     style="display:inline-block;width:336px;height:280px"
     data-ad-client="ca-pub-3910332370256182"
     data-ad-slot="7729934152"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script>
</section></p>
<section class="visible-sm text-center adsense_1">
	<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
	<!-- ado2 -->
	<ins class="adsbygoogle"
	     style="display:inline-block;width:728px;height:90px"
	     data-ad-client="ca-pub-3910332370256182"
	     data-ad-slot="4432584957"></ins>
	<script>
	(adsbygoogle = window.adsbygoogle || []).push({});
	</script>
</section></p>
<section class="hidden-sm hidden-xs text-center adsense_1">
<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<!-- ラージビッグ -->
<ins class="adsbygoogle"
     style="display:inline-block;width:970px;height:90px"
     data-ad-client="ca-pub-3910332370256182"
     data-ad-slot="6370841759"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script>
</section>
	</div>

<!--
	    <div class="well">
			<h3><%=pageInfo%></h3>
			<div class="row">
				<div class="col-lg-4">
				<%=pageContents%>
				</div>
			</div>
		</div>
-->
    <div class="panel panel-success">
      <div class="panel-heading">
        <h3 class="panel-title"><%=pageInfo%></h3>
      </div>
      <div class="panel-body">
        <%=pageContents%>
      </div>
    </div>
	<div id="well">

<section class="visible-xs text-center adsense_1">
<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<!-- モバイル5 -->
<ins class="adsbygoogle"
     style="display:inline-block;width:336px;height:280px"
     data-ad-client="ca-pub-3910332370256182"
     data-ad-slot="7729934152"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script>
</section></p>
<section class="visible-sm text-center adsense_1">
	<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
	<!-- ado2 -->
	<ins class="adsbygoogle"
	     style="display:inline-block;width:728px;height:90px"
	     data-ad-client="ca-pub-3910332370256182"
	     data-ad-slot="4432584957"></ins>
	<script>
	(adsbygoogle = window.adsbygoogle || []).push({});
	</script>
</section></p>
<section class="hidden-sm hidden-xs text-center adsense_1">
<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<!-- ラージビッグ -->
<ins class="adsbygoogle"
     style="display:inline-block;width:970px;height:90px"
     data-ad-client="ca-pub-3910332370256182"
     data-ad-slot="6370841759"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script>
</section>
	</div>

	</div>
 <br />
 <br />

</div>
<!-- フッターは#wrapの外に -->



<div id="footer">
 <div class="container">
  <%="your access from "%><%=userAgentName%><%=" "%><%=footerInfo%>
 </div>
</div>
</body>
</html>
<%}%>
