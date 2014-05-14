<%@ page session="false" %>
<%@ page pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <header>
        <nav class="navbar navbar-default" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
            	<a href="<c:url value='../j_spring_security_logout' />">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">Logout</button>
                </a>
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">Menu</button>
                 <a class="navbar-brand" href="/portal/main/zins-work-shop/top">Zin's Work Shop</a><br>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav">
				    <!-- <li class="dropdown">
				        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Gallery <b class="caret"></b></a>
				        <ul class="dropdown-menu">
				            <li><a href="/portal/main/zins-work-shop/gallery">Sky</a></li>
				            <li><a href="/portal/main/zins-work-shop/gallery">Clay</a></li>
				            <li><a href="/portal/main/zins-work-shop/gallery">Pet</a></li>
				            <li><a href="/portal/main/zins-work-shop/gallery">Other</a></li>
				        </ul>
				    </li> -->
				    <li class="dropdown">
				        <a href="#" class="dropdown-toggle" data-toggle="dropdown">古銭 <b class="caret"></b></a>
				        <ul class="dropdown-menu">
				        	<li><a href="/portal/main/zins-work-shop/oldcoin">マイコレクション</a></li>
				            <li><a href="/portal/main/zins-work-shop/oldcoinmaster">渡来銭検索</a></li>
				        </ul>
				    </li>
				    <!-- <li class="dropdown">
				        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Xstitch <b class="caret"></b></a>
				        <ul class="dropdown-menu">
				            <li><a href="/portal/main/zins-work-shop/xstitch">DotPic</a></li>
				            <li><a href="/portal/main/zins-work-shop/xstitch">Other</a></li>
				        </ul>
				    </li>
				    <li class="dropdown">
				        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Game <b class="caret"></b></a>
				        <ul class="dropdown-menu">
				            <li><a href="/portal/main/zins-work-shop/game">Abyss</a></li>
				            <li><a href="/portal/main/zins-work-shop/game">Playing</a></li>
				        </ul>
				    </li> -->
                </ul>
            </div>
        </nav>
    </header>