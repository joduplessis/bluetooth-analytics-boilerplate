<?php 

	$con = mysqli_connect("127.0.0.1","root","jodup","massive");
	
	$data = $_GET['data'] ;
	$json_data = json_decode($data, true); 

	$user_id = $json_data['user_id'];
	$user_id_from_email = getUserIDFromEmail($user_id,$con) ;
	$device_id = $json_data['device_id'];
	$event = $json_data['event'];
	$properties = $json_data['properties'];

	// insert event

	$sql = "INSERT INTO event (user_id, device_id, event) VALUES ('$user_id_from_email', '$device_id', '$event')" ;
	$query = mysqli_query($con,$sql) or die('Error: ' . mysqli_error($con));
	$event_id = mysqli_insert_id($con) ;

	// insert the properties

	foreach ( $properties as $key => $value ) {
		mysqli_query($con,"INSERT INTO property (event_id, property, value) VALUES ('$event_id', '$key', '$value')") or die('Error: ' . mysqli_error($con));
	}

	// close the connections 

	mysqli_close($con);

	// get the user email
	
	function getUserIDFromEmail($email,$con) {

		$query = mysqli_query($con, "SELECT * FROM user WHERE email = '$email'") ;
		$returnedID = -1 ;
				
		while( $row = mysqli_fetch_array($query) ) {

			$returnedID = $row['id'] ;
			
		}	

		return $returnedID ;

	}

?>



