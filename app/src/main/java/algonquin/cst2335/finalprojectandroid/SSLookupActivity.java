/*
 * Purpose: This file is the main activity for the Sunrise & Sunset Lookup App.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid;

import static algonquin.cst2335.finalprojectandroid.R.string.error_fetching_data;
import static algonquin.cst2335.finalprojectandroid.R.string.location_saved_successfully;

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


/**
 * The main activity for the Sunrise & Sunset Lookup App. It allows users to input latitude and longitude to
 * find sunrise and sunset times, save locations to favorites, and view saved locations. It also includes a help
 * menu with instructions on how to use the app.
 * @author Yiyi Cheng
 */
public class SSLookupActivity extends AppCompatActivity {
    /**
     * EditText for inputting latitude. Users should enter the latitude value for the location they want to lookup.
     */
    private EditText latitudeEditText;

    /**
     * EditText for inputting longitude. Users should enter the longitude value for the location they want to lookup.
     */
    private EditText longitudeEditText;

    /**
     * Button to trigger the lookup of sunrise and sunset times based on the entered latitude and longitude.
     */
    private Button lookupButton;

    /**
     * Button to save the currently displayed location and its sunrise and sunset times into the favorites list.
     */
    private Button saveLocationButton;

    /**
     * Button to view the list of saved favorite locations along with their sunrise and sunset times.
     */
    private Button viewFavoritesButton;

    /**
     * TextView to display the sunrise time for the given location.
     */
    private TextView sunriseTextView;

    /**
     * TextView to display the sunset time for the given location.
     */
    private TextView sunsetTextView;

    /**
     * TextView to display the formatted location name based on the reverse geocoded latitude and longitude.
     */
    private TextView timezoneTextView;

    /**
     * RecyclerView to display the list of saved favorite locations.
     */
    private RecyclerView favoritesRecyclerView;

    /**
     * SharedPreferences to store and retrieve user preferences such as the last entered latitude and longitude.
     */
    private SharedPreferences sharedPreferences;

    /**
     * String to hold the formatted location name for the current latitude and longitude.
     */
    private String formattedLocation;

    /**
     * AppDatabase instance for Room database access, used to persist favorite locations.
     */
    private AppDatabase db;

    /**
     * LocationDao instance for performing CRUD operations on the location entities in the Room database.
     */
    private LocationDao locationDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sslookup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_app_name);
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

        // Initialize Room database and RecyclerView
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();


        locationDao = db.locationDao();
    }

    /**
     * Looks up the sunrise and sunset times for the given latitude and longitude.
     */
    private void lookupSunriseSunset() {
        String latitude = latitudeEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();

        sharedPreferences.edit()
                .putString("latitude", latitude)
                .putString("longitude", longitude)
                .apply();

        String sunriseSunsetUrl = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&date=today&timezone=UTC";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, sunriseSunsetUrl, null, response -> {
                    try {

                        JSONObject results = response.getJSONObject("results");
                        String sunrise = results.getString("sunrise");
                        String sunset = results.getString("sunset");

                        sunriseTextView.setText(getString(R.string.sunrise_) + sunrise);
                        sunsetTextView.setText(getString(R.string.sunset_) + sunset);
                        lookupLocation(latitude, longitude);
                    } catch (Exception e) {
                        Toast.makeText(SSLookupActivity.this, error_fetching_data, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(SSLookupActivity.this, error_fetching_data, Toast.LENGTH_SHORT).show());

        // Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    /**
     * Looks up the location based on latitude and longitude using a reverse geocoding service.
     */
    private void lookupLocation(String latitude, String longitude) {
        String locationUrl = "https://nominatim.openstreetmap.org/reverse?format=json&lat="
                + latitude + "&lon=" + longitude + "&zoom=5";

        JsonObjectRequest locationRequest = new JsonObjectRequest
                (Request.Method.GET, locationUrl, null, response -> {
                    String displayName = response.optString("display_name", "Unnamed Location");
                    formattedLocation = displayName;
                    timezoneTextView.setText(getString(R.string.location_) + formattedLocation);
                }, error -> {
                    formattedLocation = "Unnamed Location";
                    timezoneTextView.setText(getString(R.string.location_) + formattedLocation);
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(locationRequest);
    }


    /**
     * Saves the location with its latitude, longitude, and formatted address.
     */
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
                    Snackbar.make(findViewById(R.id.main), location_saved_successfully, Snackbar.LENGTH_LONG).show();
                    loadFavorites();
                });
            }).start();

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_latitude_or_longitude, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays the list of saved locations from the database.
     */
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

    /**
     * Loads the saved locations into the RecyclerView.
     */
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
//        loadFavorites();
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

    /**
     * Shows the help dialog with instructions for using the app.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.instruction_title);
        builder.setMessage(getString(R.string.instruction_body_welcome) +
                getString(R.string.instruction_body_how_to_use) +
                getString(R.string.instuction_body_how_to_use_1) +
                getString(R.string.instuction_body_how_to_use_2) +
                getString(R.string.instuction_body_how_to_use_3) +
                getString(R.string.instuction_body_how_to_use_4) +
                getString(R.string.instuction_body_tips) +
                getString(R.string.instuction_body_tips_1) +
                getString(R.string.instuction_body_tips_2) +
                getString(R.string.instuction_body_ask_for_assistance));
        builder.setPositiveButton(R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}