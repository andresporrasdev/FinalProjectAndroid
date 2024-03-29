package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import algonquin.cst2335.finalprojectandroid.databinding.ActivityFavoriteSongDetailBinding;

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

        db = Room.databaseBuilder(getApplicationContext(), SongDatabase.class, "song-database").build();
        executor = Executors.newSingleThreadExecutor();


        Intent intent = getIntent();
        songId = intent.getIntExtra("SONG_ID", -1);
        if (songId == -1) {
            Snackbar.make(binding.getRoot(), "No song ID provided", Snackbar.LENGTH_SHORT).show();
            return;  }

        loadSongDetails(songId);
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
        builder.setMessage(R.string.help_message);


        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());


        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void loadSongDetails(int songId) {
        executor.execute(() -> {
            Song song = db.songDao().findById(songId);
            runOnUiThread(() -> {
                if (song != null) {

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

