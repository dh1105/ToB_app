<?php
 

 include 'config.inc.php';
 $response = array();

  
// check for post data
 if(isset($_POST['states']) && isset($_POST['min_budget']) && $_POST['max_budget'] && $_POST['activity']){
    $sname = $_POST['states'];
    $min_b = $_POST['min_budget'];
    $max_b = $_POST['max_budget'];
    $activity = $_POST['activity'];

 
        // get resorts from resort table
    $sql = 'SELECT resorts.resort_name , room_prices.2A FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :sname) and activities.activity_name = :activity between :min_b and :max_b order by room_prices.2A';

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':sname', $sname, PDO::PARAM_STR);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_b', $min_b, PDO::PARAM_STR);
    $stmt->bindParam(':max_b', $max_b, PDO::PARAM_STR);
    $stmt->execute();

    if($stmt->rowCount()){
        
        $response["resorts"] = array();
        $response["room_prices"] = array();

        $response['resorts'] = $stmt->fetch(PDO::FETCH_ASSOC);
        $response['room_prices'] = $stmt->fetch(PDO::FETCH_ASSOC);

        //$response = $stmt->fetch_all();

        $response["success"] = 1;
    }

    elseif(!$stmt->rowCount()){

        $response["success"] = 0;
        $response["message"] = "No resort found";

    }
}

    echo print(json_encode($response));
   
?>