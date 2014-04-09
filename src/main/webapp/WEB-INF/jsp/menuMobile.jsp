<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div data-role="navbar" id="menu-wrap">
<table><tr><td>
<img src="<c:url value="/resources/img/banner.png" />" />
</td></tr>
<tr><td>
 <ul id="dropmenu">
  <li><a href="/portal/main/zins-work-shop/info">Info　</a></li>
  <li><a href="/portal/main/zins-work-shop/gallery">Gallery　</a></li>         
  <li><a href="/portal/main/zins-work-shop/oldcoin">OldCoin　</a></li>
  <li><a href="/portal/main/zins-work-shop/xstitch">Xstitch　</a></li>
  <li><a href="/portal/main/zins-work-shop/game">Game　</a></li>
  <li><a href="<c:url value='../j_spring_security_logout' />">Logout　</a></li>
 </ul>
</td></tr></table>
</div>