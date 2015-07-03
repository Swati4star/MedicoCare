<?php
	$error_messages = array(
		"Could Not Process your Request, Try Again Later",
		"Order Already Accepted By Some Other Store",
		"Success"
	);
	define('API_ACCESS_KEY', 'AIzaSyACU_7PNxU13Mz-GFd4UmKsENr_TnacbG0');
	$json = $_SERVER['HTTP_JSON'];
	
	if(!isset($json)){
		header('Location: index.php');
	} else {
		require_once('inc/connection.inc.php');
		
		$data = json_decode($json);
		$orderID = $data->orderID;
		$storeID = $data->storeid;
		$currentTime = time();
		$orderFlag = 1;
		
		$query = "SELECT `accepted`,`user_device` FROM `order_log` WHERE `order_id`='$orderID'";
		if($query_run = mysqli_query($connection,$query)){
			while($query_row = mysqli_fetch_assoc($query_run)){
				$orderFlag = intval($query_row['accepted']);
				$userDeviceID = $query_row['user_device'];
			}
			if($orderFlag != 1){
				$query = "UPDATE `order_log` SET `accepted`='1', `accepted_time`='$currentTime', `chemist_id`='$storeID' WHERE `order_id`='$orderID'";
				mysqli_query($connection,$query);
				
				$registrationIds = array($userDeviceID);
				$query = "SELECT * FROM `stores` WHERE `code`='$storeID'";
				if($query_run = mysqli_query($connection,$query)){
					while($query_row = mysqli_fetch_assoc($query_run)){
						$storeName = $query_row['name'];
						$storeAddress = $query_row['address'];
						$storeContact = $query_row['contact'];
					}
				}
				$order = array(
					"info" => "StoreName : ".$storeName."\nAddress : ".$storeAddress."\nContact : ".$storeContact
				);
				
				$result = sendPushNotification($registrationIds , $order);
				$error = 2;
			} else {
				$error = 1;
			}
		} else {
			$error = 0;
		}
		
		if($error != 2)
			echo $error_messages[$error];
		else
			echo $result;
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
?>