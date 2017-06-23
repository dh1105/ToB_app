<?php

include 'config.inc.php';

$response = array();

if(isset($_POST['resort_name'])){

	$resort_name = $_POST['resort_name'];

	$sql = "SELECT activities.activity_name FROM resorts inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id WHERE resort_name = :resort_name";
	$stmt = $conn->prepare($sql);
    $stmt->bindParam(':resort_name', $resort_name, PDO::PARAM_STR);
    $stmt->execute();
    if($stmt->rowCount()){

    	$response["activity_name"] = array();

    	$i = 0;
    	while($row = $stmt->fetch($result)){
            $response["activity_name"][$i] = $row["activity_name"];
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