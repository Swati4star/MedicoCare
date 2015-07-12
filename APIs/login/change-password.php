<?php
	$error_messages = array(
		"Passwords do not Match",
		"Success",
		"Could Not Process Your Request Right Now, try Again"
	);
	require_once('../inc/header.inc.php');
	if(!isset($_SESSION['user']) || empty($_SESSION['user'])){
		session_destroy();
		header('Location: /');
	} else {
		require_once('../inc/connection.inc.php');
		$mobileNumber = $_SESSION['user'];
		if(!isset($Name)){
			$query = "SELECT `first_name`,`last_name` FROM `users` WHERE `phone`='$mobileNumber'";
			if($query_run = mysqli_query($connection,$query)){
				if(mysqli_num_rows($query_run) == 1 ){
					while($query_row = mysqli_fetch_assoc($query_run)){
						$Name = ucwords($query_row['first_name']).' '.ucwords($query_row['last_name']);
					}
				} else {
					session_destroy();
					header('Location: /');
				}
			}
		}
		if(isset($_POST['submit'])){
			$password = md5(substr(md5($_POST['password']),0,30));
			$confirmPassword = md5(substr(md5($_POST['confirm-password']),0,30));
			if($password != $confirmPassword){
				$error = 0;
			} else {
				$query = "UPDATE `users` SET `password`='$password' WHERE `phone`='$mobileNumber'";
				if($query_run = mysqli_query($connection,$query)){
					$error = 1;
				} else {
					$error = 2;
				}
			}
		}
	}
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="author" content="prabhakar gupta">
	
	<link rel="shortcut icon" href="">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<link href='http://fonts.googleapis.com/css?family=Roboto:400,700' rel='stylesheet' type='text/css'>
	<title>Forget Password | MediCoHome</title>
	<style>body{font-family: 'Roboto', sans-serif;}</style>
</head>
<body>
	<div class="container" style="margin-top:10%">
		<div class="row text-center">
<?php
	if(@$error == 1){
		session_destroy();
?>
			<div class="col-md-6 col-md-offset-3">
			<h2 style="color:green">Password Successfully Changed!</h2>
			<hr>
			<a href="/">GO Home</a>
			</div>
<?php
		die();
	} elseif(isset($error)) {
		echo '<center><p class="bg-danger"><br>'.$error_messages[$error].'<br><br></p></center>';
	}
?>
			<div class="col-md-6 col-md-offset-3">
				<h2>Hello, <?php echo $Name;?>.</h2>
				Please enter your new password here :
				<hr>
				<form class="form-horizontal" method="POST" action>
					<div class="form-group">
						<input type="password" name="password" class="form-control" placeholder="Enter New Password">
					</div>
					<div class="form-group">
						<input type="password" name="confirm-password" class="form-control" placeholder="Confirm Password">
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<button name="submit" type="submit" class="btn btn-lg btn-primary">Change Password</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>