/*
 * FileName: DeezerSongActivity.java
 * Purpose: Main activity for Deezer song search application. Handles artist searches
 * and navigation to song details and favorites list.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityDeezerSongBinding;


/**
 * DeezerSongActivity is the main activity that allows users to search for artists and view their songs.
 * It utilizes SharedPreferences to remember the last search between app launches and provides
 * navigation to view song details and favorite songs list.
 *
 * @author Jiaxin Yan
 * @lab_section 022
 * @creation_date 03/28/2024
 */
public class DeezerSongActivity extends AppCompatActivity {
    /**
     * Binding instance for accessing the activity's views.
     */
    private ActivityDeezerSongBinding binding;
    /**
     * A list to hold song data retrieved from the Deezer API.
     */
    private List<Song> songs = new ArrayList<>();
    /**
     * Shared preferences to store and retrieve the last search query.
     */
    private SharedPreferences sharedPreferences;
    /**
     * The name of the SharedPreferences file where preferences are stored.
     */
    private static final String PREFS_NAME = "DeezerSongPrefs";
    /**
     * The key for storing the last search query in SharedPreferences.
     */
    private static final String PREF_LAST_SEARCH = "pref_last_search";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.Deezer_Song);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        setupRecyclerView();
        restoreLastSearch();

        binding.searchButton.setOnClickListener(v -> {
            String artist = binding.artistText.getText().toString();
            searchArtists(artist);
            saveLastSearch(artist);
        });

 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);

        return true;
    }
    /**
     * Restores the last search input by the user from SharedPreferences.
     * If there was no previous search, the search field is set to an empty string.
     */
    private void restoreLastSearch() {
        String lastSearch = sharedPreferences.getString(PREF_LAST_SEARCH, "");
        binding.artistText.setText(lastSearch);
    }
    /**
     * Saves the user's current search input to SharedPreferences for future reference.
     *
     * @param search The search query input by the user.
     */
    private void saveLastSearch(String search) {
        sharedPreferences.edit().putString(PREF_LAST_SEARCH, search).apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.my_favorite) {
            Intent intent = new Intent(this, SongFav.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.info){
            showHelpDialog();
            return true;
        }else if(item.getItemId() == R.id.goHome){
            Intent intent = new Intent(this, DeezerSongActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Shows a dialog to the user with help information on how to use the application.
     * The dialog displays a message explaining the different functionalities and
     * provides a "Close" button that dismisses the dialog.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_title);
        builder.setMessage(R.string.help_message);

        builder.setPositiveButton(R.string.close_text, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * Sets up the RecyclerView with a LinearLayoutManager and an anonymous adapter. This adapter is responsible
     * for creating view holders for items, binding song data to these views, and handling click events which
     * start the DeezerSongDetailActivity with the selected song's details.
     */
    private void setupRecyclerView() {
        binding.artistItem.setLayoutManager(new LinearLayoutManager(this));
        binding.artistItem.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                Song song = songs.get(position);
                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(song.getTitle());
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(DeezerSongActivity.this, DeezerSongDetailActivity.class);
                    intent.putExtra("TITLE", song.getTitle());
                    intent.putExtra("DURATION", song.getDuration());
                    intent.putExtra("ALBUM_NAME", song.getAlbumName());
                    intent.putExtra("ALBUM_COVER_URL", song.getAlbumCoverUrl());
                    startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return songs.size();
            }
        });
    }
    /**
     * Initiates a search for artists on the Deezer API using the provided artist name.
     * If successful, it retrieves the tracklist URL for the first artist found and calls
     * {@code fetchTracklist} to retrieve the songs. If no artists are found or there is
     * an error, it displays a Toast message to the user.
     *
     * @param artist The artist's name to search for.
     */
    private void searchArtists(String artist) {
        String url = "https://api.deezer.com/search/artist/?q=" + artist;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray artists = jsonObject.getJSONArray("data");
                if (artists.length() > 0) {
                    JSONObject firstArtist = artists.getJSONObject(0);
                    String tracklistUrl = firstArtist.getString("tracklist");
                    fetchTracklist(tracklistUrl);
                } else {
                    Toast.makeText(DeezerSongActivity.this,R.string.error_artist, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(DeezerSongActivity.this, R.string.error_parsing_data, Toast.LENGTH_SHORT).show();

            }
        }, error -> Toast.makeText(DeezerSongActivity.this, R.string.error_fetch_data_text, Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }
    /**
     * Fetches the tracklist from the provided URL and updates the song list. If the tracklist
     * is successfully retrieved, it parses the JSON response and adds each song to the list
     * which is then displayed by a RecyclerView adapter. If there's an error parsing the tracklist
     * or fetching the data, it shows a Toast message with the appropriate error.
     *
     * @param url The URL to fetch the tracklist from.
     */
    private void fetchTracklist(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray tracks = jsonObject.getJSONArray("data");
                songs.clear();
                for (int i = 0; i < tracks.length(); i++) {
                    JSONObject track = tracks.getJSONObject(i);
                    String title = track.getString("title");
                    String duration = track.getString("duration");
                    JSONObject album = track.getJSONObject("album");
                    String albumName = album.getString("title");
                    String albumCoverUrl = album.getString("cover");
                    songs.add(new Song(title, duration, albumName, albumCoverUrl));
                }
                binding.artistItem.getAdapter().notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(DeezerSongActivity.this, R.string.error_parsing_tracklist, Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(DeezerSongActivity.this, R.string.error_fetch_tracklist, Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }
}