<html>
<head>
	<script type="text/javascript" src="jquery.js"></script>
</head>
<body>
	<script type="text/javascript">
		var pin = '<%= request.getParameter( "pin" )%>';
		var mode = '<%= request.getParameter( "mode" )%>';
		function submitFunction() {
				$( "#submissionForm" ).attr( "action", "/Upload?pin=1234"  );
			};
	</script>
	
	<form id="submissionForm" enctype="multipart/form-data" onSubmit="submitFunction()" action="Upload?pin=1234" method="post">
		File: <input type="file" name="file"></input>
		Description: <input type="textarea" name="description"></input>
		<input type="text" name="appAction" value="deploy"></input>
		<input type="submit" value="Upload"></input>
	</form>
</body>
</html>