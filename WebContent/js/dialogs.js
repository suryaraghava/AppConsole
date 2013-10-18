$( "#dialog-deploy-form" ).dialog({
	autoOpen: false,
	height: 300,
	width: 350,
	modal: true,
	buttons: {
		"Upload Application": function() {
			// TODO validate
			
			$("#deploySubmissionForm").attr( "action", "/uploadServlet?pin=" + pin );
			$("#deploySubmissionForm").submit();
			$(this).dialog( "close" );

		},
		Cancel: function() {
			$(this).dialog( "close" );
		} 
	},			
	close: function() {
		allFields.val( "" ).removeClass( "ui-state-error" );
	}
});

$( "#dialog-update-form" ).dialog( {
	autoOpen: false,
	height: 300,
	width: 350,
	modal: true,
	buttons: {
		"Update Application": function() {
			// TODO validate : User should be barred from uploading
			// a zip that differs in name from existing application.
			// Excluding pin # of course.
			
			$("#updateSubmissionForm").attr( "action", "/uploadServlet?pin=" + pin );
			$("#updateSubmissionForm").submit();
		},
		Cancel: function() {
			$(this).dialog( "close" );
		},
	},			
	close: function() {
		
	}
} );