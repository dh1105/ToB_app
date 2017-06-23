<?php
 
/*
 * Following code will single resort details
 */
 
// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/db_connection.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
$sname = $_POST['states'];
$min_b = $_POST['min_budget'];
$max_b = $_POST['max_budget'];
$activity = $_POST['activity'];

 
        // get resorts from resort table
$result = "SELECT resorts.resort_name , room_prices.2A FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = $sname) and activities.activity_name = $activity between $min_b and $max_b order by room_prices.2A";


if (mysqli_num_rows($result) > 0) {

    $response["resorts"] = array();
    $response["room_prices"] = array();
    	
 	$i = 0;
    while($row = mysqli_fetch_array($result)){
        $response["resorts"][$i] = $row["resort_name"];
        $i++;
    }

    $j = 0;
    while($row = mysqli_fetch_array($result)){
        $response["room_prices"][$j] = $row["2A"];
        $j++;
    }

    $response["success"] = 1;
 
        
    echo print(json_encode($response));
} else {
    // no resort found
    $response["success"] = 0;
    $response["message"] = "No resort found";
 
    // echo no users JSON
    echo print(json_encode($response));
}
   
?>