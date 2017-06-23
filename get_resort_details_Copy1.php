<?php
 
/*
 * Following code will single resort details
 */
 
// array for JSON response
//var_dump($_REQUEST);


$servername = "localhost";
$username = "root";
$password = "tripoffbeat";
$dbname = "hotel_booking_v1";

$con = mysqli_connect($servername,$username,$password,$dbname);

// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$response = array();

if(isset($_POST['states']) && isset($_POST['min_budget']) && $_POST['max_budget'] && $_POST['activity']){  
 
    // check for post data
    $states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];

 
    // get resorts from resort table
    $sql = "SELECT resorts.resort_name , room_prices.2A FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = '$states') and activities.activity_name = '$activity' and room_prices.2A between $min_budget and $max_budget order by room_prices.2A";


    $result = mysqli_query($con, $sql);


    if(!empty($result)){

        if (mysqli_num_rows($result) > 0) {

            $response["resorts"] = array();
            $response["room_prices"] = array();
    	
            $i = 0;
            while($row = mysqli_fetch_array($result)){
                $response["resorts"][$i] = $row["resort_name"];
                $response["room_prices"][$i] = $row["2A"];
                $i++;
            }


            $response["success"] = 1;
            echo json_encode($response);
 
        
        } else {
            // no resort found
            $response["success"] = 0;
            $response["message"] = "No resort found";
            echo json_encode($response);

        }
    } else {
        $response["success"] = 0;
        $response["message"] = "No";
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Parameter missing";
    echo json_encode($response);
}

   
?>