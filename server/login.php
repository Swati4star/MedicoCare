<?php	
	$json = $_SERVER['HTTP_JSON'];
	if(!isset($json)){
		header('Location: index.php');
	} else {
		require_once('inc/connection.inc.php');
		$data = json_decode($json);

		$phonenumber = $data->phonenumber;
		$password = md5(substr(md5($data->pass),0,30));
		
		$query = "SELECT `user_id`,`first_name`,`last_name` FROM `users` WHERE `phone`='$phonenumber' AND `password`='$password'";
		$query_run = mysqli_query($connection,$query);
		if(mysqli_num_rows($query_run) == 1 ){
			$error = 1;
			while($query_row = mysqli_fetch_assoc($query_run)){
				$sendArray = array('success' => 1, 'id' => $query_row['user_id'], 'first name' => $query_row['first_name'], 'last name' => $query_row['last_name']);
				echo json_encode($sendArray);
			}
		} else {
			$sendArray = array('success' => 0, 'message' => 'Invalid Credentials');
			echo json_encode($sendArray); 
		}
	}
?>