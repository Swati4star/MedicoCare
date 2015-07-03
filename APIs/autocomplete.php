<?php
	require_once 'inc/connection.inc.php';
	$json = $_SERVER['HTTP_JSON'];
	if(!isset($json)){
		header('Location: index.php');
	} else {
		$data = json_decode($json);
		$stringSearched = $data->stringq;
		$return_arr = array();
	
		$query = "SELECT * FROM `medicines` WHERE `generic` LIKE '$stringSearched%' ORDER BY `generic` ASC";
		$query_run = mysqli_query($connection,$query);
		while($query_row = mysqli_fetch_assoc($query_run)){
			$row_array['medicine_name'] = $query_row['generic'];
			array_push($return_arr,$row_array);
		}
		echo json_encode(array('suggest_medicine'=>$return_arr));
	}
?>