<?php
	define('API_ACCESS_KEY', 'AIzaSyBTb_3pCN53EpoF--b0RvWp3FDExlCmBPo');
	$json = $_SERVER['HTTP_JSON'];
	
	if(!isset($json)){
		header('Location: index.php');
	} else {
		require_once('inc/connection.inc.php');
		
		$data = json_decode($json);
		
		$address 	= $data->address;
		$order 		= $data->order;
		$coord1 	= $data->coord1;
		$coord2 	= $data->coord2;
		$deviceID 	= $data->regId;
		$userID 	= $data->userid;
		$prescrip 	= $data->preid;
		
		$currentTime = time();
		$storeIDs = array();
		
		$query = "INSERT INTO `order_log` (`user_id`, `user_device`, `order_text`, `order_time`, `coord1`, `coord2`, `address`,`prescription`) VALUES ('$userID', '$deviceID', '$order', '$currentTime', '$coord1', '$coord2','$address','$prescrip')";
		if($query_run = mysqli_query($connection,$query)){

			$query = "SELECT `order_id` FROM `order_log` WHERE `order_time` = '$currentTime'";
			if($query_run = mysqli_query($connection,$query)){
				while($query_row = mysqli_fetch_assoc($query_run)){
					$orderID = $query_row['order_id'];
				}
			}
			
			$query = "SELECT `device_id`,`coord1`,`coord2` FROM `stores`";
			if($query_run = mysqli_query($connection,$query)){
				while($query_row = mysqli_fetch_assoc($query_run)){
					$coord1DB = $query_row['coord1'];
					$coord2DB = $query_row['coord2'];
					$deviceIdDB = $query_row['device_id'];
					if(getDistance($coord1 , $coord2 , $coord1DB , $coord2DB) <= 5000){
						array_push($storeIDs,$deviceIdDB);
					}
				}
			}
			$order = array(
				"orderID" => $orderID,
				"address" => $address,
				"order" => $order
			);
			if(empty($storeIDs)){
				echo 'No Chemists in your surroundings';
			} else {
				echo sendPushNotification($storeIDs , $order);
			}
		} else {
			echo 'Some Error. Try Again';
		}
	}

	function sendPushNotification ($Ids , $message){
		$fields = array(
			'registration_ids' => $Ids,
			'data' => $message
		);
 
		$headers = array(
			'delay_while_idle' => true,
			'Authorization: key=' . API_ACCESS_KEY,
			'Content-Type: application/json'
		);
 
		$ch = curl_init();
		curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
		curl_setopt( $ch,CURLOPT_POST, true );
		curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
		curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
		curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
		curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
		$result = curl_exec($ch);
		curl_close( $ch );

		return $result;
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