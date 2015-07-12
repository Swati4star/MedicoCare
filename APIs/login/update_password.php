<?php
	require_once '../inc/header.inc.php';
	$error_messages = array(
		"Correct",
		"Invalid Page Access Attempt"
	);
	require_once '../inc/connection.inc.php';
	
	if(isset($_GET['mob']) & isset($_GET['code'])){
		$mobileUser = $_GET['mob'];
		$codeUser = $_GET['code'];
		$query = "SELECT * FROM `forgetpass` WHERE `mobile`='$mobileUser' AND `code`='$codeUser'";
		if($query_run = mysqli_query($connection,$query)){
			if(mysqli_num_rows($query_run) != 1 ){
				header('Location: /');
			} else {
				$query1 = "DELETE FROM `forgetpass` WHERE `mobile`='$mobileUser' AND `code`='$codeUser'";
				if($query_run1 = mysqli_query($connection,$query1)){
					$_SESSION['user'] = $mobileUser;
					header('Location: change-password.php');
				} else {
					header('Location: /');
				}
			}
		} else {
			header('Location: /');
		}
	} else {
		header('Location: /');
	}
?>