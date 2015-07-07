<?php
	$json = $_SERVER['HTTP_JSON'];
	
	if(!isset($json)){
		header('Location: index.php');
	} else {
		require_once('inc/connection.inc.php');
		
		$data = json_decode($json);
		$chemistID = $data->storeid;
		
		$query = "SELECT `coord1`,`coord2` FROM `stores` WHERE `code`='$chemistID'";
		if($query_run = mysqli_query($connection,$query)){
			while($query_row = mysqli_fetch_assoc($query_run)){
				$chemistCoordinate1 = $query_row['coord1'];
				$chemistCoordinate2 = $query_row['coord2'];
			}
			
			$ordersArray = array();
			$query = "SELECT * FROM `order_log` WHERE `accepted`=0 ORDER BY `order_time` ASC";
			if($query_run = mysqli_query($connection,$query)){
				while($query_row = mysqli_fetch_assoc($query_run)){
					$coord1DB = $query_row['coord1'];
					$coord2DB = $query_row['coord2'];
					
					if(getDistance($chemistCoordinate1 , $chemistCoordinate2 , $coord1DB , $coord2DB) <= 5000){
						$tempArray = array(
							'id' 		=> intval($query_row['order_id']),
							'order' 	=> $query_row['order_text'],
							'address'	=> $query_row['address'],
							'preid'		=> $query_row['prescription']
						);
						array_push($ordersArray,$tempArray);
					}
				}
				echo json_encode(array('pendingorders' => $ordersArray));
			} else {
				echo 'Some Error. Try Again';
			}
		} else {
			echo 'Some Error. Try Again';
		}
	}
	
	function getDistance ($lat1 , $long1 , $lat2 , $long2) {
		$R = 6378137; // Earth mean radius in meter
		$dLat = deg2rad(abs($lat2 - $lat1));
		$dLong = deg2rad(abs($long2 - $long1));
		$a = sin($dLat / 2) * sin($dLat / 2) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * sin($dLong / 2) * sin($dLong / 2);
		$c = 2 * atan2(sqrt($a), sqrt(1 - $a));
		$d = $R * $c;
		return $d; // returns the distance in meter
	}
	
?>