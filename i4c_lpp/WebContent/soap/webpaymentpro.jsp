<%@ include file='constants.jsp' %>
<script language="JavaScript">
	function onClick(type){
		switch(type)
        {
			case 'cert':
				document.login.signature.disabled = true;
				document.login.certFile.disabled = false;
				document.login.privateKeyPassword.disabled = false;
				break;
			case 'tokens':
				document.login.signature.disabled = false;
				document.login.certFile.disabled = true;
				document.login.privateKeyPassword.disabled = true;
				break;
        }
	}
</script>

<%@ page language="java" %>

<html>
<head>
	<title>PayPal SDK - API Credentials</title>
</head>
<body alink=#0000FF vlink=#0000FF>
<br>
<center>
<font size=3 color=black face=Verdana><b>Use The Default Sandbox API Profile Or Enter Your Own Profile</b></font>
<br><br>
<b><font color="FF0000" size=3 face=Verdana>NOTE: Production code should NEVER expose API credentials in any way! They must be <br> managed securely in your application.</font></b>
<br><br>
<span id=normal>
<font size=3 color=black face=Verdana>
To generate a Sandbox API Certificate, follow these steps: <a href="https://www.paypal.com/IntegrationCenter/ic_certificate.html#step1" target="_blank">API Certificate</a> </span > </b>
</font>
<br><br>
<form action="Login.jsp" method=post enctype="multipart/form-data" name=login>
<table width="700">
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>API Username:</b></font><br/><font size=1 color=black face=Verdana>(ex: my_account_api1.paypal.com)</font></td>
		<td><font size=2 color=black face=Verdana>sdk-seller_api1.sdk.com</td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>API Password:</b></font></td>
		<td><font size=2 color=black face=Verdana>12345678</td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Encrypted API Certificate:</b></font><br><font size=1 color=black face=Verdana>PKCS12 (.p12) format</font></td>
		<td><font size=2 color=black face=Verdana>sdk-seller.p12</td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Certificate Private Key Password:</b></font><br><font size=1 color=black face=Verdana>(OpenSSL Export Password)</font></td>
		<td><font size=2 color=black face=Verdana>password</td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Environment:</b></font></td>
		<td><font size=2 color=black face=Verdana><%=testEnv %></td>
	</tr>
	<tr>
		<td align="right">&nbsp;</td>
		<td align=left>
			<input type="submit" value="Use default account" name="DefaultButton">
		</td>
	</tr>
	<tr> 
		<td colspan=3 align=center><br><b>Or enter your own profile...</b></td> 
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>API Credentials:</b></font>
		<td>
			<input type="radio" name="api-type" value="cert" onclick="onClick('cert');">Client Side SSL Certificate<br>
			<input type="radio" name="api-type" value="tokens" checked onclick="onClick('tokens');">3 Tokens Authentication<br>
			
		</td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>API Username:</b></font><br><font size=1 color=black face=Verdana>(ex: my_account_api1.paypal.com)</font></td>
		<td><input type="text" name="apiUsername" value=""><b> <font size=2  face=Verdana> Not your PayPal Email Address!</b> </font></td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>API Password:</b></font></td>
		<td><input type="password" name="apiPassword" value=""></td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Signature:</b></font></td>
		<td><input type="password" name="signature" value=""></td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Encrypted API Certificate:</b></font><br><font size=1 color=black face=Verdana>PKCS12 (.p12) format</font></td>
		<td><input type="file" name="certFile" value="" disabled></td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Certificate Private Key Password:</b></font><br><font size=1 color=black face=Verdana>(OpenSSL Export Password)</font></td>
		<td><input type="password" name="privateKeyPassword" value="" disabled></td>
	</tr>
	<tr>
		<td align="right"><font size=2 color=black face=Verdana><b>Environment:</b></font></td>
		<td><font size=2 color=black face=Verdana><%=testEnv %></td>
	</tr>
	<tr>
		<td align="right">&nbsp;</td>
		<td align=left>
			<input type="hidden" name="environment" value="<%=testEnv %>">
			<input type="submit" value="Use my account" name="custom">
		</td>
	</tr>
</table>
</form>
</body>
</html>