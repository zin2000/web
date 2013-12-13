<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%{
	//画面表示に必要な項目を取得する
	java.lang.String name1 = (java.lang.String)request.getAttribute("name1");
	java.lang.String name2 = (java.lang.String)request.getAttribute("name2");%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ファイルアップロード</title>
</head>
<body>
  アップロードするファイルを入力してください。
  <form action="upload"
    method="POST" enctype="multipart/form-data" >
    <input type="file" name="file" />
    <input type="file" name="file2" />
	<input id="name" type="text" name="name1" value="<%=name1%>" />
	<input id="name" type="text" name="name2" value="<%=name2%>" />
    <input type="submit" value="アップロード" />
  </form>
</body>
</html>
<%}%>