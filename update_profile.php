<?php
$host = "localhost"; // Replace with your MySQL host
$dbname = "carpoolu"; // Replace with your database name
$username = "root"; // Replace with your database username
$password = ""; // Replace with your database password

// Get the JSON request body
$requestBody = file_get_contents('php://input');
$data = json_decode($requestBody, true);

$password = $data['password'];
$academicYear = $data['academicYear'];
$description = $data['description'];
$studentId = $data['studentId'];

try {
    // Connect to the database
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Update the student_details table with the new password and academic year
    $stmt = $conn->prepare("UPDATE student_details SET Pass = :password, Academic_Year = :academicYear WHERE Student_ID = :studentId");
    $stmt->bindParam(':password', $password);
    $stmt->bindParam(':academicYear', $academicYear);
    $stmt->bindParam(':studentId', $studentId);
    $stmt->execute();

    // Update the user table with the new description
    $stmt = $conn->prepare("UPDATE user SET Description = :description WHERE Student_ID = :studentId");
    $stmt->bindParam(':description', $description);
    $stmt->bindParam(':studentId', $studentId);
    $stmt->execute();

    $response = array("success" => true, "message" => "Profile updated successfully");
    echo json_encode($response);

} catch (PDOException $e) {
    $response = array("success" => false, "message" => "Database error: " . $e->getMessage());
    echo json_encode($response);
}

// Close the database connection
$conn = null;
?>