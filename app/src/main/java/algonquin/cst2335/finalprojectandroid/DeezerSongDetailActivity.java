/*
 * FileName: DeezerSongDetailActivity.java
 * Purpose: Activity for displaying the details of a song from Deezer and allowing the user to add it to favorites.
 * Provides functionality to interact with the UI elements defined in the ActivityDeezerSongDetailBinding.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.bumptech.glide.Glide;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import algonquin.cst2335.finalprojectandroid.databinding.ActivityDeezerSongDetailBinding;
/**
 * DeezerSongDetailActivity
 * Activity for displaying the details of a song from Deezer and allowing the user to add it to favorites.
 * Provides functionality to interact with the UI elements defined in the ActivityDeezerSongDetailBinding.
 *
 * @author Jiaxin Yan
 * @lab_section 022
 * @creation_date 03/28/2024
 */
public class DeezerSongDetailActivity extends AppCompatActivity {
    private ActivityDeezerSongDetailBinding binding;
    private SongDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerSongDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        db = Room.databaseBuilder(getApplicationContext(),
                SongDatabase.class, "song-database").build();

        Intent intent = getIntent();
        final String title = intent.getStringExtra("TITLE");
        final String duration = intent.getStringExtra("DURATION");
        final String albumName = intent.getStringExtra("ALBUM_NAME");
        final String albumCoverUrl = intent.getStringExtra("ALBUM_COVER_URL");

        binding.titleText.setText(title);
        binding.albumText.setText(albumName);
        binding.durationText.setText(duration);
        Glide.with(this).load(albumCoverUrl).into(binding.albumCover);

        binding.addFavorite.setOnClickListener(v -> {
            final Song song = new Song(title, duration, albumName, albumCoverUrl);

            executor.execute(() -> {
                db.songDao().insert(song);
                 runOnUiThread(() -> {
                    Toast.makeText(DeezerSongDetailActivity.this, R.string.add_favorite_text, Toast.LENGTH_SHORT).show();
                });
            });
        });

        binding.goBack.setOnClickListener(v -> finish());
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
}
