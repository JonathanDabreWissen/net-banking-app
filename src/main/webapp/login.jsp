<html>
	<head>
		<title>
			Login - Net Banking
		</title>
	</head>
	<body>
		<h2>Welcome to Login</h2>
		<h3>Kindly enter your credentials</h3>
		<br>
		
		<form action="/doLogin" method="post">
		    Customer ID: <input type="text" name="user"><br>
		    Password: <input type="password" name="pwd"><br><br>
		    <input type="submit" value="Login">
		</form>
		<p style="color:red">${error}</p>
		<h4>If not signed up, kindly <a href="/register">Register</a></h4>
	</body>
</html>