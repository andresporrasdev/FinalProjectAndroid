/*
 * Purpose: This file provides the functionality for displaying the details of a location including sunrise and sunset times,
 *          and allows the user to delete the location from the database.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid.sslookup;

import static algonquin.cst2335.finalprojectandroid.R.string.error_parsing_data;
import static algonquin.cst2335.finalprojectandroid.R.string.location_deleted;
import static algonquin.cst2335.finalprojectandroid.R.string.location_not_available;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import algonquin.cst2335.finalprojectandroid.R;

/**
 * Activity to display and manage the details of a location including sunrise and sunset times.
 * Users can delete a location, which will remove it from the database.
 */
public class LocationDetailsActivity extends AppCompatActivity {
    /**
     * TextView for displaying the sunrise time.
     */
    private TextView sunriseTextView;

    /**
     * TextView for displaying the sunset time.
     */
    private TextView sunsetTextView;

    /**
     * TextView for displaying the location name.
     */
    private TextView locationTextView;

    /**
     * Latitude coordinate of the location.
     */
    private double latitude;

    /**
     * Longitude coordinate of the location.
     */
    private double longitude;

    /**
     * Button to delete the current location.
     */
    private Button deleteButton;

    /**
     * The current location entity being displayed.
     */
    private LocationEntity currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_app_name);
        setSupportActionBar(toolbar);

        sunriseTextView = findViewById(R.id.sunriseTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);
        locationTextView = findViewById(R.id.locationTextView);
        deleteButton = findViewById(R.id.deleteButton);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        String formattedLocation = getIntent().getStringExtra("formattedLocation");

        locationTextView.setText(getString(R.string.location_) + formattedLocation);

        lookupSunriseSunset();

        deleteButton.setOnClickListener(v -> deleteLocation());

        int locationId = getIntent().getIntExtra("location_id", -1);
        if (locationId != -1) {
            new Thread(() -> {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name").build();
                LocationDao locationDao = db.locationDao();

                currentLocation = locationDao.getLocationById(locationId);

            }).start();
        }
    }

    /**
     * Fetches and displays sunrise and sunset times for the current location.
     */
    private void lookupSunriseSunset() {
        String latitude = String.valueOf(this.latitude);
        String longitude = String.valueOf(this.longitude);
        String urlString = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&date=today&timezone=UTC";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlString, null, response -> {
                    try {
                        JSONObject results = response.getJSONObject("results");
                        String sunrise = results.getString("sunrise");
                        String sunset = results.getString("sunset");

                        sunriseTextView.setText(getString(R.string.sunrise_) + sunrise);
                        sunsetTextView.setText(getString(R.string.sunset_) + sunset);
                    } catch (Exception e) {
                        Toast.makeText(LocationDetailsActivity.this, error_parsing_data, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(LocationDetailsActivity.this, R.string.error_fetching_data, Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    /**
     * Deletes the current location from the database and finishes the activity.
     */
    private void deleteLocation() {
        if (currentLocation == null) {
            Toast.makeText(this, location_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").build();
            LocationDao locationDao = db.locationDao();

            locationDao.deleteLocation(currentLocation);

            // After deletion, finish the activity and go back
            runOnUiThread(() -> {
                Toast.makeText(LocationDetailsActivity.this, location_deleted, Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            });
        }).start();
    }

}