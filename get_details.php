<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include 'config.inc.php';

//var_dump($_POST);

$response = array();

if(isset($_POST['resort_name'])){

	$resort_name = $_POST['resort_name'];

	$sql = "SELECT resort_desc, latitude, longitude, dist_del, FLOOR(time_del/60) as time from resorts where resort_name = :resort_name";
	$stmt = $conn->prepare($sql);
    $stmt->bindParam(':resort_name', $resort_name, PDO::PARAM_STR);
    $stmt->execute();
    if($stmt->rowCount()){
    	$data = $stmt->fetch(PDO::FETCH_ASSOC);
    	$response["resort_desc"][0] = $data["resort_desc"];
    	$response["latitude"][0] = $data["latitude"];
    	$response["longitude"][0] = $data["longitude"];
    	$response["time"][0] = $data["time"];
    	$response["dist_del"][0] = $data["dist_del"];
    	$response["success"] = 1;
	} else {
		$response["success"] = 0;
	}
} else {
	$response["success"] = 2;
	$response["message"] = "No";
}


echo json_encode($response);

?>