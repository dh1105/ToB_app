<?php

$servername = "localhost";
$username = "root";
$password = "tripoffbeat";
$dbname = "hotel_booking_v2";

$con = mysqli_connect($servername,$username,$password,$dbname);

// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
  

		$sql="SELECT name FROM `states` where country_id = 101";
		$result=mysqli_query($con,$sql);
		while($e=mysqli_fetch_array($result)){
		$output[]=$e; 
		}	

		print(json_encode($output)); 

?>