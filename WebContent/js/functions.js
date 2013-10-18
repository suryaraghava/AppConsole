//make an OpenSocial request
function request() {
	var req = opensocial.newDataRequest();
	req.add( req.newFetchPersonRequest( opensocial.DataRequest.PersonId.VIEWER ), "req" );
	req.send( response );
};

//process a response from the request() function
function response( data ) {
	if( data.hadError() ) {
		alert( "Error: " + data.get( "req" ).getErrorMessage() );
	} else {
		id = data.get( "req" ).getData().getId();
	}
};

function getAppList(pin) {
	$.post( "/listApplications?pin=" + pin, populateAppList );
}

function populateAppList( data ) {
	noapps = false;
	appArr = '';
	try {
		appArr = JSON.parse( data );
	} catch( err ) {
		noapps = true;
	};

	var text = '';
	var counter = 0;
	
	//loop to create all the application buttons
	for ( var i in appArr ) {
		counter++;
		if( counter > 0 ) {
			text += '<h3><a href="#" id="appName' + i + '">' + counter + ": " + i + '</a></h3>';
			text += '<div>';
			text += '	<button id="delbutton' + i + '" style="float:right" class="delbutton ui-button ui-widget ui-state-default ui-corner-all" role="button" onclick="deleteApp()" title="Delete Application"><span class="ui-button-text">X</span></button>';			
			text += '	<p id="desc' + i + '">' + appArr[i] + '</p>';
			text += '	<button id="updatebutton' + i + '" class="logbutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button"><span class="ui-button-text">Update</span></button>';		
			text += '</div>';
		}
	}
	
	if( counter > 0 ) {
		$("#content").html(text);
	// This has to be done seperately, because we need the element to
	// exist in document before defining click event.
		for( var i in appArr ) {
			$( "#delbutton" + i).button( {icons:{ primary: "ui-icon-trash" }, text: false } );
			$( '#delbutton' + i).click( function () { buttonHandler( $(this).attr('id') ); } );
			$( '#updatebutton' + i).click( function () { buttonHandler( $(this).attr('id') ); } );
		}
		$( "#content" ).accordion({ autoheight: false, animated: 'slide', collapsible: 'true' });
	}
}

function deleteApp(id) {
	var selectedAppName = id;
	var offset = "delbutton".length;
	//get selected Appname
	selectedAppName = selectedAppName.substring(offset,id.length);
	//alert(selectedAppName);	

	$.post( "/removeServlet?" + selectedAppName + "=checkbox", function( data ) {
		data = jQuery.trim( data );
		//alert(data);
		if ( data == "OK" ) {
			alert( "The application has been deleted successfully." );
			//window.location = "newcvp.jsp";
		} else {
			alert("The application could not be deleted. " + data);
			//window.location = "newcvp.jsp";
		}
	} );
}