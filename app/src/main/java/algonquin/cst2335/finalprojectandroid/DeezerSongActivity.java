package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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


public class DeezerSongActivity extends AppCompatActivity {
    private ActivityDeezerSongBinding binding;
    private List<Song> songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "DeezerSongPrefs";
    private static final String PREF_LAST_SEARCH = "pref_last_search";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
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
    private void restoreLastSearch() {
        // Restore the last search from SharedPreferences
        String lastSearch = sharedPreferences.getString(PREF_LAST_SEARCH, "");
        binding.artistText.setText(lastSearch);
    }

    private void saveLastSearch(String search) {
        // Save the last search into SharedPreferences
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
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_title);
        builder.setMessage(R.string.help_message);

        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
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
                    Toast.makeText(DeezerSongActivity.this, "No artists found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(DeezerSongActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(DeezerSongActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }

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
                Toast.makeText(DeezerSongActivity.this, "Error parsing tracklist", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(DeezerSongActivity.this, "Error fetching tracklist", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }
}