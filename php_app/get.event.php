<?php 

	$con = mysqli_connect("127.0.0.1","root","jodup","massive");

	$user_id = $_GET['user'] ;

	$event = "";
	$event_id = 0;
	$properties = "" ;
	$output = "" ;

	$runonce = true ;

	// get the event

	$equery = mysqli_query($con, "SELECT * FROM event WHERE user_id = '$user_id' ORDER BY time DESC") ;
			
	while( $row = mysqli_fetch_array($equery) ) {
		if ($runonce) {
			$event_id = $row['id'] ;
			$event = $row['event'] ;
			$runonce = false ;
		}
	}	

	// get the properties

	$pquery = mysqli_query($con, "SELECT * FROM property WHERE event_id = '$event_id'") ;
			
	while( $row = mysqli_fetch_array($pquery) ) {
		$properties .= "You viewed product with " . $row['property']." ".$row['value'] ;
	}

	 // output

	$output = '{"heading":"'.$event.'","description":"'.$properties.'"}';

	echo $output ;
	
?>








