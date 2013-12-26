<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div data-role="navbar" id="menu-wrap">
<table><tr><td>
<img src="<c:url value="/resources/img/banner.png" />" />
</td></tr>
<tr><td>
 <ul id="dropmenu">
  <li><a href="/zins-work-shop/main/info">Info</a>
   <ul>
    <li><a href="#">子メニュー</a></li>
   </ul>
  </li>
  <li><a href="/zins-work-shop/main/gallery">Gallery</a></li>         
  <li><a href="/zins-work-shop/main/oldcoin">OldCoin</a></li>
  <li><a href="/zins-work-shop/main/xstitch">Xstitch</a></li>
  <li><a href="/zins-work-shop/main/game">Game</a></li>
 </ul>
</td></tr></table>
</div>