<?php
	$error_messages = array(
		"Could Not Process your Request, Try Again Later",
		"Could Not Send the mail right now, Try Again Later",
		"Success"
	);
	require_once 'inc/connection.inc.php';
	
	$json = $_SERVER['HTTP_JSON'];
	if(!isset($json)){
		header('Location: index.php');
	} else {
		$data = json_decode($json);
		$phoneSearched = $data->phone;
		//$phoneSearched = '9810181713';
		
		$query = "SELECT * FROM `users` WHERE `phone`='$phoneSearched'";
		if($query_run = mysqli_query($connection,$query)){
			while($query_row = mysqli_fetch_assoc($query_run)){
				$emailSearched = $query_row['email'];
				$FirstName = ucwords($query_row['first_name']);
				$LastName = ucwords($query_row['last_name']);
			}
		
			$codeGenerated = strtoupper(substr(md5(rand()), 0, 16));
			$timestamp = time();
			
			$query_log = "INSERT INTO `forgetpass` (`mobile`, `code`,`time`) VALUES ('$phoneSearched', '$codeGenerated','$timestamp')";
			if($query_run_log = mysqli_query($connection,$query_log)){
				$to = $emailSearched;
				$subject = 'Password reset request for your Medicohome account?';
				$headers = 'From: noreply@medicohome.com';
				$body = 'Dear '.$FirstName.' '.$LastName.', 

You recently requested for your password to be reset for your MediCoHome account and you can do so by clicking on the link - http://api.medicohome.com/login/update_password.php?mob='.$phoneSearched.'&code='.$codeGenerated.'

If you did not request a password reset, do not worry. No action is required at your end; you may continue using Medicohome account with your old password.

Many thanks, 
Team Medicohome';
				if(mail($to , $subject , $body, $headers)){ 
					$error = 2;
				} else {
					$error = 1;
				}
			} else {
				$error = 0;
			}
		} else {
			$error = 0;
		}
		echo $error_messages[$error];
	}
?>