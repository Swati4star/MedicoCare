<?php
	
	$dir = 'prescriptions/';
	
	if(!isset($_REQUEST['image'])){
		header('Location: index.php');
	} else {
		$base = $_REQUEST['image'];
		$current_timestamp = time();
		$current_timestamp = (string)$current_timestamp;
		$filename = $current_timestamp . '-' . basename($_REQUEST['filename']);
		
		$binary = base64_decode($base);
		header('Content-Type: bitmap; charset=utf-8');
		$file = fopen($dir . $filename, 'wb');
		
		fwrite($file, $binary);
		fclose($file);
		
		header('Content-Type: text/html');
		echo $filename;
	}
?>