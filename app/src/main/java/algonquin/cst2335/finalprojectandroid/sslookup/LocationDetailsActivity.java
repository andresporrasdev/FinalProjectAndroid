package algonquin.cst2335.finalprojectandroid.sslookup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import algonquin.cst2335.finalprojectandroid.R;

public class LocationDetailsActivity extends AppCompatActivity {
    private TextView sunriseTextView, sunsetTextView, locationTextView;
    private double latitude, longitude;

    private Button deleteButton;
    private LocationEntity currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sunrise & Sunset Lookup App");
        setSupportActionBar(toolbar);

        sunriseTextView = findViewById(R.id.sunriseTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);
        locationTextView = findViewById(R.id.locationTextView);
        deleteButton = findViewById(R.id.deleteButton);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        String formattedLocation = getIntent().getStringExtra("formattedLocation");

        locationTextView.setText("Location: " + formattedLocation);

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

                        sunriseTextView.setText("Sunrise: " + sunrise);
                        sunsetTextView.setText("Sunset: " + sunset);
                    } catch (Exception e) {
                        Toast.makeText(LocationDetailsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(LocationDetailsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


    private void deleteLocation() {
        if (currentLocation == null) {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "database-name").build();
            LocationDao locationDao = db.locationDao();

            locationDao.deleteLocation(currentLocation);

            // After deletion, finish the activity and go back
            runOnUiThread(() -> {
                Toast.makeText(LocationDetailsActivity.this, "Location deleted", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            });
        }).start();
    }

}