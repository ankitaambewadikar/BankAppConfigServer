<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>${message}</h1>
<form action="transfer" method="post" >
	Enter Sender Account Number: <input name="senderAccountNumber"/><br/>
	Enter Receiver Account Number: <input name="receiverAccountNumber"/><br/>
	Enter Amount : <input name="amount"/><br/>
	<input type="submit"/>
</form>
<a href="statement?offset=0&size=5">Get Statement</a>
</body>
</html>