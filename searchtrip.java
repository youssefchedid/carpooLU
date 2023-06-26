package com.example.carpoolu20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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


public class searchtrip extends AppCompatActivity {
    private Spinner facultySpinner;
    private Spinner citySpinner;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private Button searchButton;
    private TableLayout resultTable;

    private List<String> facultyList;
    private List<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchtrip);

        facultySpinner = findViewById(R.id.select_faculty);
        citySpinner = findViewById(R.id.select_city);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        searchButton = findViewById(R.id.search);

        // Fetch faculty and city data from the server
        fetchFacultyData();
        fetchCityData();

        // Set up the click listener for the share button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected faculty, city, and radio button values
                String selectedFaculty = facultySpinner.getSelectedItem().toString();
                String selectedCity = citySpinner.getSelectedItem().toString();
                int selectedFacultyFlag = radioButton1.isChecked() ? 1 : 0;

                // Perform validation for the selected values
                if (selectedFaculty.isEmpty() || selectedCity.isEmpty()) {
                    Toast.makeText(searchtrip.this, "Please select faculty and city", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the rest of the input values (date, number of seats, price per person)
                String selectedDateAndTime = ""; // Get the selected date and time

                // Call the searchTrip method to send the data to the server
                searchTrip(selectedFaculty, selectedCity, selectedFacultyFlag, selectedDateAndTime);
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
                                ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(searchtrip.this,
                                        android.R.layout.simple_spinner_item, facultyList);
                                facultySpinner.setAdapter(facultyAdapter);
                            } else {
                                // Handle the error scenario appropriately
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle the error scenario appropriately
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle the error scenario appropriately
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void fetchCityData() {
        String url = "http://192.168.8.188/php/get_city.php";

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
                                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(searchtrip.this,
                                        android.R.layout.simple_spinner_item, cityList);
                                citySpinner.setAdapter(cityAdapter);
                            } else {
                                // Handle the error scenario appropriately
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle the error scenario appropriately
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle the error scenario appropriately
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void searchTrip(String faculty, String city, int facultyFlag, String dateAndTime) {
        String url = "http://192.168.8.188/php/search_trip.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("faculty", faculty);
            postData.put("city", city);
            postData.put("faculty_flag", facultyFlag);
            postData.put("date_and_time", dateAndTime);
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the error scenario appropriately
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // Store the search results in a JSONArray
                                JSONArray resultsArray = new JSONArray();
                                for (int i = 0; i < resultTable.getChildCount(); i++) {
                                    TableRow row = (TableRow) resultTable.getChildAt(i);
                                    JSONObject result = new JSONObject();
                                    try {
                                        result.put("Driver_User_ID", ((TextView) row.getChildAt(0)).getText().toString());
                                        result.put("Number_Of_Seats", Integer.parseInt(((TextView) row.getChildAt(1)).getText().toString()));
                                        result.put("Flag_faculty", Integer.parseInt(((TextView) row.getChildAt(2)).getText().toString()));
                                        result.put("Date_And_Time", ((TextView) row.getChildAt(3)).getText().toString());
                                        result.put("Price_Per_Passenger", Double.parseDouble(((TextView) row.getChildAt(4)).getText().toString()));
                                        resultsArray.put(result);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Start the new activity to display the search results
                                Intent intent = new Intent(searchtrip.this, result.class);
                                intent.putExtra("results", resultsArray.toString());
                                startActivity(intent);
                            } else {
                                // Handle the error scenario appropriately
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle the error scenario appropriately
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Handle the error scenario appropriately
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
