package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityFavoriteSongDetailBinding;

//public class FavoriteSongDetail extends AppCompatActivity {
//    private ActivityFavoriteSongDetailBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityFavoriteSongDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Retrieve the data passed from the previous activity
//        Intent intent = getIntent();
//        String title = intent.getStringExtra("TITLE");
//        String duration = intent.getStringExtra("DURATION");
//        String albumName = intent.getStringExtra("ALBUM_NAME");
//        String albumCoverUrl = intent.getStringExtra("ALBUM_COVER_URL");
//
//        // Set the data to the appropriate views
//        binding.favTitleText.setText(title);
//        binding.favAlbumText.setText(albumName);
//        binding.favDurationText.setText(duration);
//        Glide.with(this).load(albumCoverUrl).into(binding.favAlbumCover);


//       private void deleteSong() {
//        executor.execute(() -> {
//            Log.d("FavoriteSongDetail", "Attempting to delete song with ID: " + songId);
//            // Get the song from the database
//            Song song = songDao.findById(songId);
//
//            // Check if the song was found
//            if (song != null) {
//                Log.d("FavoriteSongDetail", "Found song: " + song.getTitle());
//                songDao.delete(song);
//                // Use runOnUiThread to perform actions on the UI thread
//                runOnUiThread(() -> {
//                    // Notify user of success
//                    Toast.makeText(FavoriteSongDetail.this, "Song deleted", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(binding.getRoot(), "Song deleted", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", v -> undoDelete(song))
//                            .show();
//                    new Handler().postDelayed(this::finish, Snackbar.LENGTH_LONG);
//                });
//            } else {
//                Log.d("FavoriteSongDetail", "Song not found with ID: " + songId);
//                runOnUiThread(() -> Toast.makeText(FavoriteSongDetail.this, "Song not found", Toast.LENGTH_SHORT).show());
//            }
//        });
//    }
//    }
//
//
//}

public class FavoriteSongDetail extends AppCompatActivity {
    private ActivityFavoriteSongDetailBinding binding;
    private SongDatabase db;
    private ExecutorService executor;
    private int songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteSongDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        // Initialize database and executor
        db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song-database").build();
        executor = Executors.newSingleThreadExecutor();

        // Retrieve extras
        Intent intent = getIntent();
        songId = intent.getIntExtra("SONG_ID", -1);
        if (songId == -1) {
            Snackbar.make(binding.getRoot(), "No song ID provided", Snackbar.LENGTH_SHORT).show();
            return; // Exit the activity if no valid song ID is provided
        }

        loadSongDetails(songId); // Implement this method to load song details from the database

        binding.delete.setOnClickListener(v -> deleteSong(songId));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);

        return true;
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
        builder.setMessage(R.string.help_message); // 假设你在strings.xml中定义了帮助信息

        // 设置关闭按钮
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        // 创建并显示AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void loadSongDetails(int songId) {
        executor.execute(() -> {
            Song song = db.songDao().findById(songId);
            runOnUiThread(() -> {
                if (song != null) {
                    // Update UI with song details
                    binding.favTitleText.setText(song.getTitle());
                    binding.favAlbumText.setText(song.getAlbumName());
                    binding.favDurationText.setText(song.getDuration());
                    Glide.with(this).load(song.getAlbumCoverUrl()).into(binding.favAlbumCover);
                } else {
                    Snackbar.make(binding.getRoot(), "Song not found", Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void deleteSong(int songId) {
        executor.execute(() -> {
            Song song = db.songDao().findById(songId);
            if (song != null) {
                db.songDao().delete(song);
                runOnUiThread(() -> {
                    Snackbar.make(binding.getRoot(), "Song deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", undoView -> undoDelete(song))
                            .show();
                });
            }
        });
    }

    private void undoDelete(Song song) {
        executor.execute(() -> {
            db.songDao().insert(song);
            runOnUiThread(() -> Snackbar.make(binding.getRoot(), "Undo successful", Snackbar.LENGTH_SHORT).show());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (db.isOpen()) {
            db.close();
        }
    }
}

