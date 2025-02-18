<html>
	
	<head>
		<title>
			Register - Net Banking
		</title>
	</head>
	<body>
		
		<h3>Register for Net Banking</h3>
		<form action="/doRegister" method="post">
		    Customer Id: <input type="text" name="customer_id"><br>
		    Name: <input type="text" name="name"><br>
		    Password: <input type="password" name="pwd"><br>
		    Confirm Password: <input type="password" name="con_pwd"><br>
		    <input type="submit" value="Register">
		</form>
		<p style="color:red">${error}</p>
	</body>
</html>