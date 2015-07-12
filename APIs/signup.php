<?php	
	$error_message = array(
		"Phone Number Already Registered",
		"Could Not Register, Try Again Later",
		"Registered Successfully"
	);
	$json = $_SERVER['HTTP_JSON'];
	if(!isset($json)){
		header('Location: index.php');
	} else {
		require_once('inc/connection.inc.php');
		$data = json_decode($json);

		$first_name = $data->fname;
		$last_name = $data->lname;
		$phonenumber = $data->phonenumber;
		$password = md5(substr(md5($data->pass),0,30));
		$emailID = strtolower($data->email);
		$registrationID = $data->regId;
		
		$query = "SELECT `user_id` FROM `users` WHERE `phone`='$phonenumber'";
		if($query_run = mysqli_query($connection,$query)){
			if(mysqli_num_rows($query_run) > 0 ){
				$error = 0;
			} else {
				$query = "INSERT INTO `users` (`first_name`,`last_name`,`email`,`phone`,`password`) VALUES ('$first_name','$last_name','$emailID','$phonenumber','$password')";
				if(!mysqli_query($connection,$query))
					$error = 1;
				else {
					$error = 2;
				}
			}
		} else {
			$error = 1;
		}		
		
		echo $error_message[$error];
	}
?>
