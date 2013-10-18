<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<script type="text/javascript" src="js/jquery.js"></script>
	<style type="text/css" media="screen">@import url(css/style.css)</style>
</head>
<body>
	<div id="content">
		<table class="appList" id="appList">
			<tr class="header">
				<th>Select</th>
				<th>Name</th>
				<th>Description</th>
			</tr>
			<tr class="app">
				<td class="checkBox"><input name="appSelectionRadioButton" value="Dummy App 1" type="radio" class="checkbox"/></td>
				<td class="appName">Dummy App 1</td>
				<td class="appDesc">Dummy App 1 description.</td>
			</tr>
			<tr class="app">
				<td class="checkbox"><input name="appSelectionRadioButton" value="Dummy App 2" type="radio"/></td>
				<td class="appName">Dummy App 2</td>
				<td class="appDesc">Dummy App 2 description. This application has a particiularly long description that is being used to test that the box responds intelligently to long as well as short descriptions. I hope this is not a problem. </td>
			</tr>
			<tr class="app">
				<td class="checkbox"><input name="appSelectionRadioButton" value="Dummy App 3" type="radio"/></td>
				<td class="appName">Dummy App 3</td>
				<td class="appDesc">Dummy App 3 description.</td>
			</tr>

			<tr class="app">
				<td class="checkbox"><input name="appSelectionRadioButton" value="Dummy App 4" type="radio"/></td>
				<td class="appName">Dummy App 4</td>
				<td class="appDesc">Dummy App 4 description.</td>
			</tr>
			<tr class="buttons">
				<td></td><td></td>
				<td>
					<div class="buttonRow">
						<input class="button" type="button" value="Delete"></input>
						<input class="button" type="button" value="Upload" onclick="uploadApp"></input>	
					</div>
				</td>
			</tr>
		</table>
	</div>

		<script type="text/javascript">
			var APP_NAME = 0;
			var APP_DESCRIPTION = 1;
			var uidText = parent.parent.is_userId;
			var pin;
			$.get('pin2.jsp?uid=' + uidText, function(data) {
				pin = jQuery.trim(data);
				getAppList(pin);
			});

			function getAppList(pin) {
				$.post("/listApplications?pin=" + pin, populateAppList);
			}


			function deleteApp() {
				var selectedAppName;

				// get selected Appname
				selectedAppName = $("input:radio[name='appSelectionRadioButton']:checked").val();
				//alert(selectedAppName);	

				$.post("deleteApplication.jsp?" + selectedAppName + "=checkbox", function (data) {
					document.write(data);	
				});

			}

			function populateAppList(data) {
				var noapps = false;
				try { 
					var appArr = JSON.parse(data);
			} catch (err) {
					noapps = true;
			};

				var content = ($('#content').get(0));
				content.innerHTML = '';
				var text = '<table class="appList" id="appList">';
				text += '<tr class="header">';
				text +=  '		<th>Select</th>';
				text += '				<th>Name</th>';
				text += '				<th>Description</th>';
				text += '		</tr>';
				if (noapps) {
					text += '<tr><td>No applications deployed. Click "Upload" to deploy an application.</td></tr>';
			} else {
				for (var i in appArr) {
					text += '<tr class=app>';
					text += '<td class="checkbox"><input name="appSelectionRadioButton" value="'+i+'" type="radio"></input>';  
					text += '<td class="appName">' + i + '</td>';
					text += '<td class="appDesc">' + appArr[i] + '</td>';
					text += '</tr>';
			}
			}
			text += '<tr class="buttons">';
			text += '<td></td><td></td>'
			text += '	<td>';
			text += ' 	<div class="buttonRow">';
			text += '			<input class="button" type="button" value="Delete" onclick="deleteApp()"></input>';
			text += '			<input class="button" type="button" value="Upload" onclick="uploadApp('+ pin +')"></input>	'
			text += '		</div>';
			text += '	</td>';
			text += '</tr>';
			text += '</table>';
			$(content).append(text);
			}

			function uploadApp(pin) {
				window.location = "upload.jsp?pin=" + pin;
			}

			// Explicit call to debug. Normally, this will be called after pin
			// is retrieved.
			//getAppList(3333);
	</script>
</body>
</html>
