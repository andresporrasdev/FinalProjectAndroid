package algonquin.cst2335.finalprojectandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import  androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import algonquin.cst2335.finalprojectandroid.sslookup.AppDatabase;
import algonquin.cst2335.finalprojectandroid.sslookup.LocationAdapter;
import algonquin.cst2335.finalprojectandroid.sslookup.LocationDao;
import algonquin.cst2335.finalprojectandroid.sslookup.LocationEntity;

public class SSLookupActivity extends AppCompatActivity {
    private EditText latitudeEditText, longitudeEditText;
    private Button lookupButton, saveLocationButton, viewFavoritesButton;
    private TextView sunriseTextView, sunsetTextView, timezoneTextView;

    private RecyclerView favoritesRecyclerView;
    private SharedPreferences sharedPreferences;
    private String formattedLocation;

    AppDatabase db;
    private LocationDao locationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sslookup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sunrise & Sunset Lookup App");
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        lookupButton = findViewById(R.id.lookupButton);
        saveLocationButton = findViewById(R.id.saveLocationButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);
        sunriseTextView = findViewById(R.id.sunriseTextView);
        sunsetTextView = findViewById(R.id.sunsetTextView);
        timezoneTextView = findViewById(R.id.locationTextView);


        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocationAdapter adapter = new LocationAdapter(this, new ArrayList<>());
        favoritesRecyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("SSLookupPrefs", MODE_PRIVATE);
        latitudeEditText.setText(sharedPreferences.getString("latitude", ""));
        longitudeEditText.setText(sharedPreferences.getString("longitude", ""));

        lookupButton.setOnClickListener(v -> lookupSunriseSunset());
        saveLocationButton.setOnClickListener(v -> saveLocation(formattedLocation));
        viewFavoritesButton.setOnClickListener(v -> viewFavorites());

        // Initialize Room database and RecyclerView here
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();


        locationDao = db.locationDao();
    }

    private void lookupSunriseSunset() {
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();

        sharedPreferences.edit()
                .putString("latitude", latitude)
                .putString("longitude", longitude)
                .apply();

        String sunriseSunsetUrl = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&date=today&timezone=UTC";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, sunriseSunsetUrl, null, response -> {
                    try {
                        JSONObject results = response.getJSONObject("results");
                        String sunrise = results.getString("sunrise");
                        String sunset = results.getString("sunset");

                        sunriseTextView.setText("Sunrise: " + sunrise);
                        sunsetTextView.setText("Sunset: " + sunset);
                        lookupLocation(latitude, longitude);
                    } catch (Exception e) {
                        Toast.makeText(SSLookupActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(SSLookupActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void lookupLocation(String latitude, String longitude) {
        String locationUrl = "https://nominatim.openstreetmap.org/reverse?format=json&lat="
                + latitude + "&lon=" + longitude + "&zoom=5";

        JsonObjectRequest locationRequest = new JsonObjectRequest
                (Request.Method.GET, locationUrl, null, response -> {
                    String displayName = response.optString("display_name", "Unnamed Location");
                    formattedLocation = displayName;
                    timezoneTextView.setText("Location: " + formattedLocation);
                }, error -> {
                    formattedLocation = "Unnamed Location";
                    timezoneTextView.setText("Location: " + formattedLocation);
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(locationRequest);
    }



    private void saveLocation(String formattedLocation) {
        String latitudeStr = latitudeEditText.getText().toString();
        String longitudeStr = longitudeEditText.getText().toString();

        try {
            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);

            LocationEntity location = new LocationEntity();
            location.latitude = latitude;
            location.longitude = longitude;
            location.formattedLocation = formattedLocation;

            new Thread(() -> {
                locationDao.insertLocation(location);
                runOnUiThread(() -> {
//                    Toast.makeText(SSLookupActivity.this, "Location saved", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.main), "Location saved successfully", Snackbar.LENGTH_LONG).show();
                    loadFavorites();
                });
            }).start();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid latitude or longitude", Toast.LENGTH_SHORT).show();
        }
    }




    private void viewFavorites() {
        new Thread(() -> {
            List<LocationEntity> locations = locationDao.getAllLocations();
            runOnUiThread(() -> {
                LocationAdapter adapter = (LocationAdapter) favoritesRecyclerView.getAdapter();
                if (adapter != null) {
                    adapter.setLocations(locations);
                } else {
                    // In case the adapter is null
                    adapter = new LocationAdapter(SSLookupActivity.this, locations);
                    favoritesRecyclerView.setAdapter(adapter);
                }
            });
        }).start();
    }
    private void loadFavorites() {
        new Thread(() -> {
            List<LocationEntity> locations = locationDao.getAllLocations();
            runOnUiThread(() -> {
                LocationAdapter adapter = (LocationAdapter) favoritesRecyclerView.getAdapter();
                if (adapter != null) {
                    adapter.setLocations(locations);
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh the list every time the activity resumes
        loadFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sslookup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.help_menu_item) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        builder.setMessage("Welcome to the Sunrise & Sunset Lookup App! This application allows you to find the sunrise and sunset times for any location specified by its latitude and longitude.\n\n" +
                "How to Use:\n" +
                "- Enter Latitude and Longitude: In the provided text fields, enter the latitude and longitude for which you wish to find sunrise and sunset times. Latitude and longitude should be in decimal format.\n" +
                "- Lookup Sunrise and Sunset Times: Press the 'Lookup' button to retrieve the sunrise and sunset times for the entered location.\n" +
                "- Save a Location: If you wish to save the location for future reference, press the 'Save Location' button after looking up the times. This will store the location in your favorites list.\n" +
                "- View Saved Locations: Click on 'View Favorites' to see a list of all saved locations along with their sunrise and sunset times. You can click on any location in the list to view more details or delete it from the favorites.\n\n" +
                "Tips:\n" +
                "- Make sure the latitude and longitude are correct to get accurate sunrise and sunset times.\n" +
                "- Use negative values for latitudes south of the equator and longitudes west of the Prime Meridian.\n\n" +
                "Should you need further assistance or encounter any issues, please feel free to contact our support team.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}