
//define the delete button icon, with no text
$(function() {
	$( "delbutton" ).button( 
		{ icons:{ primary: "ui-icon-trash" }, text: false }
	);
});

//Recieves the ID of the button being clicked. 
//Should be something like: delbuttonRentalCarDemo
function buttonHandler( id ) {
	if ( id == 'uploadButton' ) {
		$( "#dialog-deploy-form" ).dialog( "open" );			
	} else if ( id.indexOf( 'delbutton' ) != -1 ) {
		deleteApp( id );
	} else if ( id.indexOf( 'updatebutton' ) != -1 ) {
		var descID = id;
		var appNameID = id;
		var offset = "updatebutton".length + 1;
		appNameID = appNameID.substring( appNameID.indexOf( 'descupdatebutton' ) + offset, appNameID.length );
		descID = "#desc" + descID.substring( descID.indexOf( 'descupdatebutton' ) + offset, descID.length );
		var description = $( descID ).html();
		$( "#updateDescription" ).val( description );
		$( "#updateAppName" ).val( appNameID );
		$( "#dialog-update-form" ).dialog( "open" );			
	}
}

$( '#uploadButton' ).click( function () {
	buttonHandler( $(this).attr('id') );
} );

$( document ).ready( new function() {
	$.get( 'pin2.jsp?uid=' + uidText, function( data ) {
		pin = jQuery.trim( data );
		getAppList( pin );
	} );
} );