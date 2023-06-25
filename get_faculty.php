<?php
$host = "localhost"; // Replace with your MySQL host
$dbname = "carpoolu"; // Replace with your database name
$username = "root"; // Replace with your database username
$password = ""; // Replace with your database password

try {
    // Connect to the database
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $query = "SELECT Faculty_Name FROM faculty";
    $result = $conn->query($query);

    $response = array();

    if ($result->rowCount() > 0) {
        $faculties = array();
        while ($row = $result->fetch(PDO::FETCH_ASSOC)) {
            $faculties[] = $row['Faculty_Name'];
        }
        $response['success'] = true;
        $response['faculties'] = $faculties;
    } else {
        $response['success'] = false;
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