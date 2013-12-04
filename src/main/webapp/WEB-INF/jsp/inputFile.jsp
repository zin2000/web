<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ファイルアップロード</title>
</head>
<body>
  アップロードするファイルを入力してください。
  <form action="upload"
    method="POST" enctype="multipart/form-data" >
    <input type="file" name="file" />
    <input type="submit" value="アップロード" />
  </form>
</body>
</html>