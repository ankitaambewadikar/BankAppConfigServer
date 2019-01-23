<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>${message}</h1>
<form action="deposit" method="post">
	Enter Account Number: <input name="accountNumber"/><br/>
	Enter Amount : <input name="amount"/><br/>
	<input type="submit"/>
</form>
<a href="statement?offset=0&size=5">Get Statement</a>
	
</body>
</html>