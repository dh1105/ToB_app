<?php

include 'config.inc.php';

$response = array();

if(isset($_POST['resort_name'])){

	$resort_name = $_POST['resort_name'];

	$sql = "SELECT room_prices.2A, resorts.dist_del, states.name FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id WHERE resort_name = :resort_name";
	$stmt = $conn->prepare($sql);
    $stmt->bindParam(':resort_name', $resort_name, PDO::PARAM_STR);
    $stmt->execute();
    if($stmt->rowCount()){

    	$response["price"] = array();
    	$response["dist_del"] = array();
    	$response["name"] = array();

    	$i = 0;
    	while($row = $stmt->fetch($result)){
            $response["price"][$i] = $row["2A"];
            $response["dist_del"][$i] = $row["dist_del"];
            $response["name"][$i] = $row["name"];
            $i++;
        }


        $response["success"] = 1;

    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }
}

echo json_encode($response);
?> 