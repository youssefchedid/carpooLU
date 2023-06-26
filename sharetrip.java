package com.example.carpoolu20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class sharetrip extends AppCompatActivity {
        private Spinner facultySpinner;
        private Spinner citySpinner;
        private RadioButton radioButton1;
        private RadioButton radioButton2;
        private Button shareButton;
        private EditText numberOfSeats;
        private EditText pricePerPerson;

        private List<String> facultyList;
        private List<String> cityList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sharetrip);

           facultySpinner = findViewById(R.id.select_faculty);
           citySpinner = findViewById(R.id.select_city);
           radioButton1 = findViewById(R.id.radioButton1);
           radioButton2 = findViewById(R.id.radioButton2);
           shareButton = findViewById(R.id.share);
           numberOfSeats=findViewById(R.id.seats);
           pricePerPerson=findViewById(R.id.price);


            // Fetch faculty and city data from the server
            fetchFacultyData();
            fetchCityData();

            // Set up the click listener for the share button
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected faculty, city, and radio button values
                    String selectedFaculty = facultySpinner.getSelectedItem().toString();
                    String selectedCity = citySpinner.getSelectedItem().toString();
                    int selectedFacultyFlag = radioButton1.isChecked() ? 1 : 0;

                    // Perform validation for the selected values
                    if (selectedFaculty.isEmpty() || selectedCity.isEmpty()) {
                        Toast.makeText(sharetrip.this, "Please select faculty and city", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get the rest of the input values (date, number of seats, price per person)
                    String selectedDateAndTime = ""; // Get the selected date and time

                    // Perform validation for the rest of the input values
                    if (selectedDateAndTime.isEmpty() || numberOfSeats == null || pricePerPerson == null) {
                        Toast.makeText(sharetrip.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Call the shareTrip method to send the data to the server
                    shareTrip(selectedFaculty, selectedCity, selectedFacultyFlag, selectedDateAndTime, numberOfSeats, pricePerPerson);
                }
            });
        }

        private void fetchFacultyData() {
            String url = "http://192.168.8.188/php/get_faculty.php";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    JSONArray facultyArray = response.getJSONArray("faculties");
                                    facultyList = new ArrayList<>();
                                    for (int i = 0; i < facultyArray.length(); i++) {
                                        String faculty = facultyArray.getString(i);
                                        facultyList.add(faculty);
                                    }
                                    // Populate the faculty spinner with the faculty list
                                    ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(sharetrip.this,
                                            android.R.layout.simple_spinner_item, facultyList);
                                    facultySpinner.setAdapter(facultyAdapter);
                                } else {
                                    // Handle the error scenario appropriately
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            // Handle error response
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

        private void fetchCityData() {
            String url = "http://10.0.2.2/carpoophp/get_city.php";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    JSONArray cityArray = response.getJSONArray("cities");
                                    cityList = new ArrayList<>();
                                    for (int i = 0; i < cityArray.length(); i++) {
                                        String city = cityArray.getString(i);
                                        cityList.add(city);
                                    }
                                    // Populate the city spinner with the city list
                                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(sharetrip.this,
                                            android.R.layout.simple_spinner_item, cityList);
                                    citySpinner.setAdapter(cityAdapter);
                                } else {
                                    // Handle the error scenario appropriately
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            // Handle error response
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

        private void shareTrip(String faculty, String city, int facultyFlag, String dateAndTime, EditText numberOfSeats, EditText pricePerPerson) {
            // Create a JSON object with the trip data
            JSONObject tripData = new JSONObject();
            try {
                tripData.put("faculty", faculty);
                tripData.put("city", city);
                tripData.put("faculty_flag", facultyFlag);
                tripData.put("date_and_time", dateAndTime);
                tripData.put("number_of_seats", numberOfSeats);
                tripData.put("price_per_person", pricePerPerson);
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON creation error
            }

            // Make a request to the server to share the trip
            String url = "http://10.0.2.2/carpoophp/share_trip.php";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, tripData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                String message = response.getString("message");

                                // Handle the server response
                                if (success) {
                                    Toast.makeText(sharetrip.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(sharetrip.this,managetrip.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(sharetrip.this, message, Toast.LENGTH_SHORT).show();
                                    // Trip sharing failed, handle the error scenario appropriately
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Handle JSON parsing error
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            // Handle error response
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }