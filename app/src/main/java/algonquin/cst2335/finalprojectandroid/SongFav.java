package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * An {@link AppCompatActivity} that displays a list of favorite songs stored in the application's database.
 * Users can view all their favorite songs, click on a song to see its details, or navigate to other parts of the application
 * using the options menu.
 *
 * The activity uses a {@link RecyclerView} to list the songs and employs an {@link ExecutorService} to fetch the song data
 * asynchronously from the database to ensure the UI remains responsive.
 */
public class SongFav extends AppCompatActivity {
    private RecyclerView favSongList;
    private List<Song> songs = new ArrayList<>();
    private SongDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * Initializes the activity, RecyclerView for displaying songs, and sets up the toolbar.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_fav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        favSongList = findViewById(R.id.favSongList);
        favSongList.setLayoutManager(new LinearLayoutManager(this));


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
                holder.songTitle.setText(song.getTitle());
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(SongFav.this, FavoriteSongDetail.class);
                    intent.putExtra("SONG_ID", song.getId());
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
        };


        favSongList.setAdapter(adapter);


        fetchSongsFromDatabase(adapter);
    }
    /**
     * Inflates the menu and adds items to the action bar if it is present.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);

        return true;
    }
    /**
     * Displays a help dialog with instructions on using the app.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_title);
        builder.setMessage(R.string.help_message);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * Handles action bar item clicks.
     *
     * @param item The menu item that was clicked.
     * @return Boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.my_favorite) {
            Intent intent = new Intent(this, SongFav.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.goHome){
            Intent intent = new Intent(this, DeezerSongActivity.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.info){
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Fetches the list of favorite songs from the database on a separate thread and updates the adapter.
     *
     * @param adapter The adapter for the RecyclerView that displays the favorite songs.
     */
    private void fetchSongsFromDatabase(final RecyclerView.Adapter<SongViewHolder> adapter) {
        executor.execute(() -> {
            songs = db.songDao().getAll();
            runOnUiThread(adapter::notifyDataSetChanged);
        });
    }


    /**
     * ViewHolder for song items in the RecyclerView.
     */
    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(android.R.id.text1);
        }
    }
    /**
     * Shuts down the executor and closes the database when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (db != null) {
            db.close();
        }
    }
}