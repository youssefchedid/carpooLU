<?php
$host = "localhost"; // Replace with your MySQL host
$dbname = "carpoolu"; // Replace with your database name
$username = "root"; // Replace with your database username
$password = ""; // Replace with your database password

// Get the email and password from the request
$email = $_POST['email'];
$password = $_POST['password'];

try {
    // Connect to the database
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Prepare the SQL statement
    $stmt = $conn->prepare("SELECT Student_ID FROM student_details WHERE UL_Mail = :email AND Pass = :password");
    $stmt->bindParam(':email', $email);
    $stmt->bindParam(':password', $password);

    // Execute the query
    $stmt->execute();

    // Fetch the result
    $result = $stmt->fetch(PDO::FETCH_ASSOC);

    // Check if a row is returned
    if ($result) {
        $studentId = $result['Student_ID'];
        $response = array("success" => true, "message" => "Login successful", "studentId" => $studentId);
        echo json_encode($response);
    } else {
        $response = array("success" => false, "message" => "Invalid email or password");
        echo json_encode($response);
    }
} catch (PDOException $e) {
    echo "Database error: " . $e->getMessage();
}

// Close the database connection
$conn = null;
?>