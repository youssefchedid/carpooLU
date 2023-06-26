<?php
$host = "localhost"; // Replace with your MySQL host
$dbname = "carpoolu"; // Replace with your database name
$username = "root"; // Replace with your database username
$password = ""; // Replace with your database password

try {
    // Connect to the database
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Retrieve the data from the POST request
    $facultyName = $_POST['faculty'];
    $cityName = $_POST['city'];
    $facultyFlag = $_POST['faculty_flag'];
    $dateAndTime = $_POST['date_and_time'];
    $numberOfSeats = $_POST['number_of_seats'];
    $pricePerPerson = $_POST['price_per_person'];

    // Validate the data (perform additional checks if necessary)
    if (empty($facultyName) || empty($cityName) || empty($facultyFlag) || empty($dateAndTime) || empty($numberOfSeats) || empty($pricePerPerson)) {
        $response['success'] = false;
        $response['message'] = "All fields are required";
    } else {
        // Get the Faculty ID
        $facultyQuery = "SELECT Faculty_ID FROM faculty WHERE Faculty_Name = ?";
        $facultyStmt = $conn->prepare($facultyQuery);
        $facultyStmt->execute([$facultyName]);
        $facultyId = $facultyStmt->fetchColumn();

        // Get the City ID
        $cityQuery = "SELECT City_ID FROM city WHERE City_Name = ?";
        $cityStmt = $conn->prepare($cityQuery);
        $cityStmt->execute([$cityName]);
        $cityId = $cityStmt->fetchColumn();

        // Select the trip data and store them into array 
        $sql = "SELECT Driver_User_ID,Flag_faculty,	Start_Date_And_Time,Number_Of_Seats,Price_Per_Passenger FROM trip WHERE Faculty-ID = '$facultyId' AND City_ID = '$cityId' AND faculty_flag = '$facultyFlag' AND date_and_time = '$dateAndTime'";
$result = $conn->query($sql);
if ($result) {
    // Fetch the search results
    $searchResults = array();
    while ($row = $result->fetch_assoc()) {
        $resultData = array(
            'Driver_User_ID' => $row['Driver_User_ID'],
            'Number_Of_Seats' => $row['Number_Of_Seats'],
            'Flag_faculty' => $row['Flag_faculty'],
            'Date_And_Time' => $row['Date_And_Time'],
            'Price_Per_Passenger' => $row['Price_Per_Passenger']
        );

        // Add the result data to the search results array
        $searchResults[] = $resultData;
    }

    $response['success'] = true;
    $response['message'] = 'Search successful';
    $response['results'] = $searchResults;
} else {
    $response['success'] = false;
    $response['message'] = 'Search failed';
}