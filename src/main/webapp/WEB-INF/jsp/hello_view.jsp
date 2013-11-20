<html>
<head>
 <jsp:include page="/WEB-INF/jsp/menu_h.jsp" flush="true" />
</head>
  <body bgcolor="white">
    <jsp:include page="/WEB-INF/jsp/menu.jsp" flush="true" />
    <br />
    <br />
    <div style="font-size: 150%; color: #850F0F">
      <span>Enter your name: </span><br />
      <form method="post" action="hello">
        <input type=text size="15" name="user" >
        <input type=submit name="submit" value="Ok">
      </form>
    </div>
    <div>
      <%
          {
            java.lang.String answer = (java.lang.String)request.getAttribute("greeting");   
      %>
      <span><%=answer%></span>
      <%
          }
      %>
    </div>
  </body>
</html>

