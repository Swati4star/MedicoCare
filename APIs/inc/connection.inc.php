<?php
	$connect_error = 'Could not connect';
	$mysql_host = 'localhost';
	$mysql_user = '';
	$mysql_pass = '';
	$mysql_data = '';
	
	if(!$connection = mysqli_connect($mysql_host , $mysql_user , $mysql_pass, $mysql_data))
		die(mysqli_error($connection));
?>
