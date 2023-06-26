package com.example.carpoolu20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class editprofile extends AppCompatActivity {

    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtAcademicYear;
    private EditText edtDescription;
    private Button btnSaveChanges;

    private static final String UPDATE_PROFILE_URL = "http://192.168.8.188/php/update_profile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        edtPassword = findViewById(R.id.password);
        edtConfirmPassword = findViewById(R.id.confirm_password);
        edtAcademicYear = findViewById(R.id.select_year);
        edtDescription = findViewById(R.id.description);
        btnSaveChanges = findViewById(R.id.submit);

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    public void saveChanges() {
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String academicYear = edtAcademicYear.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        // Perform validation checks
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the JSON request body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("password", password);
            requestBody.put("academicYear", academicYear);
            requestBody.put("description", description);
            requestBody.put("studentId", UserSession.getStudentId());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Create the RequestQueue using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create the JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_PROFILE_URL, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            Toast.makeText(editprofile.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                Toast.makeText(editprofile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(editprofile.this,welcome.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editprofile.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfileActivity", "Volley error: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}