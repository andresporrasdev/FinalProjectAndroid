/*
 * FileName: FavoriteSongDetail.java
 * Purpose: Activity for displaying the details of a favorite song stored in the application's Room database.
 * Allows users to view song details, delete a song from their favorites, and undo the deletion.
 * It is assumed that the intent launching this activity includes an extra with the key "SONG_ID"
 * indicating the ID of the song to display.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
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
/**
 * Activity for displaying the details of a favorite song stored in the application's Room database.
 * Allows users to view song details, delete a song from their favorites, and undo the deletion.
 * It is assumed that the intent launching this activity includes an extra with the key "SONG_ID"
 * indicating the ID of the song to display.
 *
 * @author Jiaxin Yan
 * @lab_section 022
 * @creation_date 03/28/2024
 */
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
            Snackbar.make(binding.getRoot(), R.string.no_song_id_text, Snackbar.LENGTH_SHORT).show();
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
     * Loads and displays the song details from the database for the given song ID. If the song is found,
     * the title, album name, duration, and album cover are set into the respective views. If the song
     * cannot be found, a Snackbar is displayed to inform the user that the song was not found.
     * This method uses an {@code ExecutorService} to perform the database operation on a background thread
     * to avoid blocking the main thread, which could cause the UI to freeze. Once the song data is retrieved,
     * it then uses {@code runOnUiThread} to update the UI elements on the main thread.
     *
     * @param songId The unique ID of the song to load details for. This should be a valid ID that exists in the database.
     */
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
                    Snackbar.make(binding.getRoot(), R.string.song_not_found, Snackbar.LENGTH_SHORT).show();
                }
            });
        });
    }
    /**
     * Deletes a song from the database asynchronously based on the song ID provided. If the song is found and
     * successfully deleted, a Snackbar is displayed with an "Undo" action that allows the user to revert the deletion.
     * The database operations are performed on a background thread provided by an {@code ExecutorService} to prevent
     * UI blocking, and UI updates are queued on the main thread using {@code runOnUiThread}.
     *
     * @param songId The ID of the song to be deleted from the database. The ID should correspond to a song
     *               that exists in the database, otherwise no action will be taken.
     */
    private void deleteSong(int songId) {
        executor.execute(() -> {
            Song song = db.songDao().findById(songId);
            if (song != null) {
                db.songDao().delete(song);
                runOnUiThread(() -> {
                    Snackbar.make(binding.getRoot(), R.string.song_deleted_text, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo_text, undoView -> undoDelete(song))
                            .show();
                });
            }
        });
    }
    /**
     * Reverses the deletion of a song by reinserting the provided song object into the database.
     * This is typically used as an "Undo" feature, allowing users to restore a song they have just deleted.
     * The operation is performed asynchronously to avoid blocking the UI thread. Upon successful reinsertion,
     * a Snackbar notification is displayed to inform the user that the undo operation was successful.
     *
     * @param song The song object to be reinserted into the database. This should be a non-null Song instance
     *             that was previously deleted from the database.
     */
    private void undoDelete(Song song) {
        executor.execute(() -> {
            db.songDao().insert(song);
            runOnUiThread(() -> Snackbar.make(binding.getRoot(), R.string.undo_successfull_text, Snackbar.LENGTH_SHORT).show());
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

