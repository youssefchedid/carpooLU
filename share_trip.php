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

        // Insert the trip data into the trip table
        $tripQuery = "INSERT INTO trip (Faculty_ID, City_ID, Faculty_Flag, Date_and_Time, Number_of_Seats, Price_Per_Person)
                      VALUES (?, ?, ?, ?, ?, ?)";
        $tripStmt = $conn->prepare($tripQuery);
        $tripStmt->execute([$facultyId, $cityId, $facultyFlag, $dateAndTime, $numberOfSeats, $pricePerPerson]);

        // Check if the trip was successfully inserted
        if ($tripStmt->rowCount() > 0) {
            $response['success'] = true;
            $response['message'] = "Trip shared successfully";
        } else {
            $response['success'] = false;
            $response['message'] = "Failed to share trip";
        }
    }

} catch (PDOException $e) {
    $response['success'] = false;
    $response['message'] = "Database error: " . $e->getMessage();
}

// Set the response headers
header('Content-Type: application/json');

// Send the JSON response
echo json_encode($response);
?>