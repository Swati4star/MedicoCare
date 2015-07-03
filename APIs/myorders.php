<?php
	$json = $_SERVER['HTTP_JSON'];
	$accepted = array(false,true);
	
	if(!isset($json)){
		header('Location: index.php');
	} else {
		require_once('inc/connection.inc.php');
		
		$data = json_decode($json);
	
		$userID = $data->userid;
		$query = "SELECT * FROM order_log A INNER JOIN stores B ON A.chemist_id=B.code WHERE A.user_id='$userID'";
		if($query_run = mysqli_query($connection,$query)){
			$ordersArray = array();
			while($query_row = mysqli_fetch_assoc($query_run)){				
				$tempArray = array(
					'id' 		=> intval($query_row['order_id']),
					'order' 	=> $query_row['order_text'],
					'accepted'	=> $accepted[$query_row['accepted']]
				);
				if($query_row['accepted']){
					$chemistArray = array(
						'store_name'	=> $query_row['name'],
						'owner_name'	=> $query_row['owner'],
						'address'		=> $query_row['address'],
						'contact'		=> $query_row['contact'],
						'time'			=> gmdate("d-m-Y h:i:s A", intval($query_row['accepted_time']))
					);
					array_push($tempArray,array('orders' => $chemistArray));
				}
				array_push($ordersArray,$tempArray);
			}
			
			echo json_encode(array('myorders' => $ordersArray));
		} else {
			echo 'Some Error. Try Again';
		}
	}
?>