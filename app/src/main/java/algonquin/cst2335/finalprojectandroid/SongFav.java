package algonquin.cst2335.finalprojectandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SongFav extends AppCompatActivity {
    private RecyclerView favSongList;
    private List<Song> songs = new ArrayList<>();
    private SongDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_fav);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        favSongList = findViewById(R.id.favSongList);
        favSongList.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song-database").build();

        RecyclerView.Adapter<SongViewHolder> adapter = new RecyclerView.Adapter<SongViewHolder>() {
            @NonNull
            @Override
            public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                return new SongViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
                Song song = songs.get(position);
                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(song.getTitle());
            }

            @Override
            public int getItemCount() {
                return songs.size();
            }
        };

        favSongList.setAdapter(adapter);
        fetchSongsFromDatabase(adapter);
    }

    private void fetchSongsFromDatabase(final RecyclerView.Adapter<SongViewHolder> adapter) {
        executor.execute(() -> {
            songs = db.songDao().getAll();
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            // The itemView is a TextView from android.R.layout.simple_list_item_1
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (db != null) {
            db.close();
        }
    }
}