<html>
	<head>
		<script src="js/jquery.js"></script>
	</head>
	<body>
		<script>
		// Should actually retrieve the app name from the request parameter using JSP. 
		// I actually did this, but comments don't rule out JSP tags. 
		var appName = <%=request.getParameter( "appName" )%>
		//var appName = '3457HelloWorld2';
		var urlString = "/removeServlet?" + appName + "=checkbox";
		$.post( urlString, function( data ){ window.location="cvp.jsp"; } );
	</script>
	</body>
</html>


