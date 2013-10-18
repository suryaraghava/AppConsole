<html style="height:100%;"><head></head><body>
<link type="text/css" href="css/overcast/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
<link type="text/css" href="css/style.css" rel="stylesheet" />
	
<script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
        <script type="text/javascript" src="js/opensocial-jquery-1.3.2.5.min.js"></script>
        <script type="text/javascript" src="js/opensocial-jquery.autoHeight-1.0.0.min.js"></script>
        <script type="text/javascript" src="js/opensocial-jquery.minimessage-1.0.0.min.js"></script>
        <script type="text/javascript" src="js/opensocial-jquery.templates-0.1.0.min.js"></script>
<script type="text/javascript" src="js/functions.js"></script>
<script type="text/javascript" src="js/dialogs.js"></script>
<script type="text/javascript" src="js/buttons.js"></script>


<script type="text/javascript">
	//var uidText = parent.is_userId;
	var m = window.parent;
	var uidText = m.is_userId;
	var pin = '';
	var debug = false;
	request();
</script>
<script type="text/javascript">
function getInternetExplorerVersion()
// Returns the version of Internet Explorer or a -1
// (indicating the use of another browser).
{
  var rv = -1; // Return value assumes failure.
  if (navigator.appName == 'Microsoft Internet Explorer')
  {
    var ua = navigator.userAgent;
    var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
    if (re.exec(ua) != null)
      rv = parseFloat( RegExp.$1 );
  }
  return rv;
}
function checkIEVersion()
{
  var msg = 0;
  var ver = getInternetExplorerVersion();

  if ( ver > -1 )
  {
    msg = 1;
  }
  return msg;
}
//IE Always has to be difficult
function IEAlertWrap(argu,argu2)
{
    if(checkIEVersion())   
    {
        resizeIframe(argu2);  
    }    
    eval(argu);
}

//works well
function resizeIframe(iframeID) 
{ 
    if(self==parent) return false; 
    var iePad = 10;    
    var test = $("#"+iframeID,window.parent.document);
    if(checkIEVersion())
    {
        var FramePageHeight = $("#appContainer").height() + iePad;
        $("#appContainer").parent().css("height",FramePageHeight).css("overflow","hidden");        
        test.css("height",($("#appContainer").parent().height())+15);
        
        if(FramePageHeight > 600)
        {
            var topL = test.closest(".static_column").css("height",test.height()+20);
        } 
    }
    else
    {
        var FramePageHeight = $("#appContainer").height() + iePad;
        test.css("height",FramePageHeight).attr("scrolling", "no");        
    }
} 
$(document).ready(function(){
	resizeIframe(window.frameElement.id); 
});
</script>

<!--  This place-holder will be filled in with new content by jQuery. -->
<div id="content"><div id="errorMsg">You have not yet uploaded any apps.</div></div>
<button id="uploadButton" class="uploadbutton ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" role="button"><span class="ui-button-text">Upload Application</span></button>		
	
<!--  These are exactly the same, aside from appAction-->	
<div id="dialog-deploy-form" title="Deploy App">
 	<form id="deploySubmissionForm" enctype="multipart/form-data" onSubmit="submitFunction()" action="/uploadServlet?pin=" method="post">
		<fieldset>
			<label for="file">File</label>
			<input type="file" name="file" id="file" class="text ui-widget-content ui-corner-all"></input>
			<label for="description">Description</label>
			<input type="hidden" name="appAction" value="deploy"></input>	
					
			<input type="text" id="deployDescription" name="description" class="text ui-widget-content ui-corner-all"></input>	
		</fieldset>
	</form>
</div>

<div id="dialog-update-form" title="Update App">
 	<form id="updateSubmissionForm" enctype="multipart/form-data" onSubmit="submitFunction()" action="/uploadServlet?pin=" method="post">
		<fieldset>
			<label for="file">File</label>
			<input type="file" name="file" id="file" class="text ui-widget-content ui-corner-all"></input>
			<label for="description">Description</label>
			<input type="hidden" name="appAction" value="update"></input>	
			<input type="hidden" name="appName" id="updateAppName"></input>		
			<input type="text" id="updateDescription" name="description" class="text ui-widget-content ui-corner-all"></input>	
		</fieldset>
	</form>
</div>
</body></html>