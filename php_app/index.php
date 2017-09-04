
<?php $con = mysqli_connect("127.0.0.1","root","jodup","massive"); ?>

<html>
	<head>
		<style>
			* {
				font-family: arial ;	
			}
			ul {
				display:block;
				padding: 0px;
				margin: 0px;
				list-style-type: none ;
			}
			li {
				width: 200px;
				float:left; 
				height: 350px;
				background-color: #fafafa;
				margin-left: 5px;	
				margin-bottom: 5px;	
			}
			h1 {
				font-size: 20px;
				padding: 0px;
				margin: 0px;
				padding-top: 10px;
				padding-left: 10px;
			}
			p {
				font-size: 14px;
				padding: 0px;
				margin: 0px;
				padding-left: 10px;
			}
			a {
				font-size: 14px;
				font-weight: bold ;
				text-decoration: none ;
				color: #ff0000;
				padding-left: 10px;
			}
			.image {
				width: 200px;
				height: 200px;
				display:block;
			}
		</style>		
		<script>

			var personID = "0" ;
			var deviceID = 0 ;

			function init() {

				// set the variables

				var email = prompt("Please enter your e-mail address", "jduplessis@mrphome.com");

				// assign the id

				if (email != null)
				    personID = email ;
				else
				personID = null ;

				// set the cookie value of this device

				if ( getCookie("massive") != 1000 )	 {
					setCookie("massive", "1000") ;	
				}	

				deviceID = getCookie("massive") ;

			}

			// product view

			function viewProduct(id) {

				var data = {
						"user_id": personID,
						"device_id": deviceID,
						"event": "product view",
						"properties": {
							"id": id 
						},
					} ;

				var str_json = JSON.stringify(data)
				var progress = httpGet("http://localhost/massive/add.event.php?data="+str_json) ;

				alert("Just viewed product id " + id) ;

			}	

			// URL requests

			function httpGet(theUrl) {

			    var xmlHttp = null;
			    xmlHttp = new XMLHttpRequest();
			    xmlHttp.open( "GET", theUrl, false );
			    xmlHttp.send( null );

			    return xmlHttp.responseText;

			}

			// cookie handlers

			function getCookie(sName) {

			    sName = sName.toLowerCase();

			    var oCrumbles = document.cookie.split(';');

			    for(var i=0; i<oCrumbles.length;i++) {
			        var oPair= oCrumbles[i].split('=');
			        var sKey = decodeURIComponent(oPair[0].trim().toLowerCase());
			        var sValue = oPair.length>1?oPair[1]:'';
			        if(sKey == sName)
			            return decodeURIComponent(sValue);
			    }

			    return '';

			}

			function setCookie(sName,sValue) {

			    var oDate = new Date();
			    oDate.setYear(oDate.getFullYear()+1);
			    var sCookie = encodeURIComponent(sName) + '=' + encodeURIComponent(sValue) + ';expires=' + oDate.toGMTString() + ';path=/';
			    document.cookie= sCookie;

			}

			function clearCookie(sName) {

			    setCookie(sName,'');

			}

		</script>
	</head>
	<body onLoad="init()">
		<ul>
			<li>
				<div style="background-color: #ff0000;" class="image"></div>
				<h1>Product #1</h1>
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
				<a href="#" onclick="viewProduct(1);">View product</a>
			</li>
			<li>
				<div style="background-color: #ff0000;" class="image"></div>
				<h1>Product #2</h1>
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
				<a href="#" onclick="viewProduct(2);">View product</a>
			</li>
			<li>
				<div style="background-color: #ff0000;" class="image"></div>
				<h1>Product #3</h1>
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
				<a href="#" onclick="viewProduct(3);">View product</a>
			</li>
			<li>
				<div style="background-color: #ff0000;" class="image"></div>
				<h1>Product #4</h1>
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
				<a href="#" onclick="viewProduct(4);">View product</a>
			</li>
			<li>
				<div style="background-color: #ff0000;" class="image"></div>
				<h1>Product #5</h1>
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
				<a href="#" onclick="viewProduct(5);">View product</a>
			</li>
		</ul>
	</body>
</html>







