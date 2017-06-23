<?php

include 'config.inc.php';

$response = array();

if(isset($_POST['states']) && isset($_POST['min_budget']) && $_POST['max_budget'] && $_POST['activity']){  
 
    // check for post data
    $states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];

 
    // get resorts from resort table
    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and room_prices.2A between :min_budget and :max_budget order by room_prices.2A";

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':states', $states, PDO::PARAM_STR);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

}elseif(isset($_POST['states']) && isset($_POST['min_budget']) && $_POST['max_budget'] && $_POST['activity'] && isset($_POST['dist_del'])){  
 
    // check for post data
    $states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];
    $dist_del = $_POST['dist_del'];

 
    // get resorts from resort table
    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and room_prices.2A between :min_budget and :max_budget and resorts.dist_del <= :dist_del order by room_prices.2A";

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':states', $states, PDO::PARAM_STR);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
    $stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

}elseif(isset($_POST['states']) && isset($_POST['min_budget']) && $_POST['max_budget'] && $_POST['activity'] && isset($_POST['time_del'])){  
 
    // check for post data
    $states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];
    $time_del = $_POST['time_del'];

 
    // get resorts from resort table
    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and room_prices.2A between :min_budget and :max_budget and resorts.time_del/60 <= :time_del order by room_prices.2A";

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':states', $states, PDO::PARAM_STR);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
    $stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

} elseif(isset($_POST['states']) && isset($_POST['min_budget']) && isset($_POST['max_budget'])) {

	$states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and room_prices.2A between :min_budget and :max_budget order by room_prices.2A";

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':states', $states, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['states']) && isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['dist_del'])) {

	$states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $dist_del = $_POST['dist_del'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and room_prices.2A between :min_budget and :max_budget and resorts.dist_del <= :dist_del order by room_prices.2A";

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':states', $states, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
    $stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['states']) && isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['time_del'])) {

	$states = $_POST['states'];
    $min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $time_del = $_POST['time_del'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and room_prices.2A between :min_budget and :max_budget and resorts.time_del/60 <= :time_del order by room_prices.2A";

    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':states', $states, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
    $stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


}elseif(isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['activity'])){

	$min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id INNER JOIN states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id WHERE activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and room_prices.2A between :min_budget and :max_budget order by room_prices.2A";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['activity']) && isset($_POST['time_del'])){

	$min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];
    $time_del = $_POST['time_del'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id INNER JOIN states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id WHERE activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and room_prices.2A between :min_budget and :max_budget and resorts.time_del/60 <= :time_del order by room_prices.2A";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
    $stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


}elseif(isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['activity']) && isset($_POST['dist_del'])){

	$min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
    $activity = $_POST['activity'];
    $dist_del = $_POST['dist_del'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id INNER JOIN states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id WHERE activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and room_prices.2A between :min_budget and :max_budget and resorts.dist_del <= :dist_del order by room_prices.2A";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
    $stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


}elseif(isset($_POST['states']) && isset($_POST['activity']) && isset($_POST['time_del'])){

	$states = $_POST['states'];
	$activity = $_POST['activity'];
	$time_del = $_POST['time_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and resorts.time_del/60 <= :time_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':states', $states, PDO::PARAM_STR);
	$stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
	$stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['states']) && isset($_POST['activity']) && isset($_POST['dist_del'])){

	$states = $_POST['states'];
	$activity = $_POST['activity'];
	$time_del = $_POST['dist_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and resorts.dist_del <= :dist_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':states', $states, PDO::PARAM_STR);
	$stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
	$stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['states']) && isset($_POST['time_del'])){

	$states = $_POST['states'];
	$time_del = $_POST['time_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and resorts.time_del/60 <= :time_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':states', $states, PDO::PARAM_STR);
	$stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }



} elseif(isset($_POST['states']) && isset($_POST['dist_del'])){

	$states = $_POST['states'];
	$dist_del = $_POST['dist_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and resorts.dist_del <= :dist_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':states', $states, PDO::PARAM_STR);
	$stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

}elseif(isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['dist_del'])){

	$min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
	$dist_del = $_POST['dist_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE room_prices.2A between :min_budget and :max_budget and resorts.dist_del <= :dist_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
	$stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
	$stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

} elseif(isset($_POST['min_budget']) && isset($_POST['max_budget']) && isset($_POST['time_del'])){

	$min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];
	$time_del = $_POST['time_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE room_prices.2A between :min_budget and :max_budget and resorts.time_del/60 <= :time_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
	$stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
	$stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

}elseif(isset($_POST['activity']) && isset($_POST['dist_del'])){

	$activity = $_POST['activity'];
	$dist_del = $_POST['dist_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and resorts.dist_del <= :dist_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
	$stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

}elseif(isset($_POST['activity']) && isset($_POST['time_del'])){

	$activity = $_POST['activity'];
	$time_del = $_POST['time_del'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) and resorts.time_del/60 <= :time_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
	$stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

}elseif(isset($_POST['states']) && isset($_POST['activity'])){

	$states = $_POST['states'];
	$activity = $_POST['activity'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) and activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':states', $states, PDO::PARAM_STR);
	$stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
	$stmt->execute();
	if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['states'])){

	$states = $_POST['states'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.state_id = (SELECT id FROM states WHERE name = :states) order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':states', $states, PDO::PARAM_STR);
	$stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

} elseif(isset($_POST['dist_del'])){

	$dist_del = $_POST['dist_del'];
	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A,  states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.dist_del <= :dist_del order by room_prices.2A";
	$stmt = $conn->prepare($sql);
    $stmt->bindParam(':dist_del', $dist_del, PDO::PARAM_INT);
    $stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

} elseif(isset($_POST['time_del'])){

	$time_del = $_POST['time_del'];
	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A,  states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE resorts.time_del/60 <= :time_del order by room_prices.2A";

	$stmt = $conn->prepare($sql);
    $stmt->bindParam(':time_del', $time_del, PDO::PARAM_INT);
    $stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


}
elseif(isset($_POST['min_budget']) && isset($_POST['max_budget'])){

	$min_budget = $_POST['min_budget'];
    $max_budget = $_POST['max_budget'];

    $sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A,  states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE room_prices.2A between :min_budget and :max_budget order by room_prices.2A";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':min_budget', $min_budget, PDO::PARAM_INT);
    $stmt->bindParam(':max_budget', $max_budget, PDO::PARAM_INT);
    $stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }


} elseif(isset($_POST['activity'])) {

	$activity = $_POST['activity'];

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join resort_activities on resorts.id = resort_activities.resort_id inner join activities on resort_activities.id = activities.id inner join states on resorts.state_id = states.id inner join cities on resorts.city_id = cities.id WHERE activities.id in (select GROUP_CONCAT(id) from activities where parent_id = (SELECT id FROM activities WHERE activity_name = :activity)) order by room_prices.2A";

	$stmt = $conn->prepare($sql);
	$stmt->bindParam(':activity', $activity, PDO::PARAM_STR);
	$stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
            $i++;
        }


        $response["success"] = 1;
    }else {
            // no resort found
        $response["success"] = 0;
        $response["message"] = "No resort found";
    }

} else{

	$sql = "SELECT DISTINCT(resorts.resort_name), room_prices.2A, states.name, cities.name as cn, resorts.dist_del FROM resorts inner join room_prices on room_prices.resort_id =resorts.id inner join states on states.id = resorts.state_id inner join cities on resorts.city_id = cities.id order by room_prices.2A";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
    if($stmt->rowCount()){
    	$response["resorts"] = array();
        $response["room_prices"] = array();
        $response["name"] = array();
        $response["cities"] = array();
        $response["dist_del"] = array();
    	
        $i = 0;
        while($row = $stmt->fetch($result)){
            $response["resorts"][$i] = $row["resort_name"];
            $response["room_prices"][$i] = $row["2A"];
            $response["name"][$i] = $row["name"];
            $response["cities"][$i] = $row["cn"];
            $response["dist_del"][$i] = $row["dist_del"];
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