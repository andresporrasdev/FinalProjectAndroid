package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

//public class DeezerSongActivity extends AppCompatActivity {
//    private ActivityDeezerSongBinding binding;
//    private List<String> trackTitles = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setupRecyclerView();
//        binding.searchButton.setOnClickListener(v -> searchArtists(binding.artistText.getText().toString()));
//    }
//
//    private void setupRecyclerView() {
//        binding.artistItem.setLayoutManager(new LinearLayoutManager(this));
//        binding.artistItem.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//            @NonNull
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
//                return new RecyclerView.ViewHolder(view) {};
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(trackTitles.get(position));
//            }
//
//            @Override
//            public int getItemCount() {
//                return trackTitles.size();
//            }
//        });
//    }
//
//    private void searchArtists(String artist) {
//        String url = "https://api.deezer.com/search/artist/?q=" + artist;
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray artists = jsonObject.getJSONArray("data");
//                        if (artists.length() > 0) {
//                            // We only want the first artist
//                            JSONObject firstArtist = artists.getJSONObject(0);
//                            // Retrieve the tracklist URL from the first artist
//                            String tracklistUrl = firstArtist.getString("tracklist");
//                            // Fetch the tracklist using the tracklist URL
//                            fetchTracklist(tracklistUrl);
//                        } else {
//                            Toast.makeText(DeezerSongActivity.this, "No artists found", Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        Log.e("DeezerSearch", "JSON parsing error: " + e.getMessage());
//                        Toast.makeText(DeezerSongActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> Toast.makeText(DeezerSongActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());
//
//        Volley.newRequestQueue(this).add(stringRequest);
//    }
//
//    private void fetchTracklist(String url) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONArray tracks = jsonObject.getJSONArray("data");
//                        trackTitles.clear();
//                        for (int i = 0; i < tracks.length(); i++) {
//                            JSONObject track = tracks.getJSONObject(i);
//                            trackTitles.add(track.getString("title"));
//                        }
//                        // Notify the adapter to refresh the list
//                        binding.artistItem.getAdapter().notifyDataSetChanged();
//                    } catch (Exception e) {
//                        Log.e("DeezerSearch", "JSON parsing error: " + e.getMessage());
//                        Toast.makeText(DeezerSongActivity.this, "Error parsing tracklist", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> Toast.makeText(DeezerSongActivity.this, "Error fetching tracklist", Toast.LENGTH_SHORT).show());
//
//        Volley.newRequestQueue(this).add(stringRequest);
//    }
//}

public class DeezerSongActivity extends AppCompatActivity {
    private ActivityDeezerSongBinding binding;
    private List<Song> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupRecyclerView();
        binding.searchButton.setOnClickListener(v -> searchArtists(binding.artistText.getText().toString()));
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
                    Intent intent = new Intent(DeezerSongActivity.this, DeezerSongDetail.class);
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