package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityDeezerSongDetailBinding;

//public class DeezerSongDetailActivity extends AppCompatActivity {
//    private ActivityDeezerSongDetailBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityDeezerSongDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        Intent intent = getIntent();
//        String title = intent.getStringExtra("TITLE");
//        String duration = intent.getStringExtra("DURATION");
//        String albumName = intent.getStringExtra("ALBUM_NAME");
//        String albumCoverUrl = intent.getStringExtra("ALBUM_COVER_URL");
//
//        // Set the song details to the TextViews and ImageView
//        binding.titleText.setText(title);
//        binding.albumText.setText(albumName);
//        binding.durationText.setText(duration);
//        Glide.with(this).load(albumCoverUrl).into(binding.albumCover);
//
//
//        binding.goBack.setOnClickListener(v -> {
//            finish();  // Just call finish() if you want to close the current activity
//        });
//
//        // If you want to start a new Activity named SongFav then use:
//        // binding.addFavorite.setOnClickListener(v -> {
//        //     Intent intent = new Intent(DeezerSongDetailActivity.this, SongFav.class);
//        //     startActivity(intent);
//        // });
//    }
//}

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
        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(),
                SongDatabase.class, "song-database").build();

        Intent intent = getIntent();
        final String title = intent.getStringExtra("TITLE");
        final String duration = intent.getStringExtra("DURATION");
        final String albumName = intent.getStringExtra("ALBUM_NAME");
        final String albumCoverUrl = intent.getStringExtra("ALBUM_COVER_URL");

        // Set the song details to the TextViews and ImageView
        binding.titleText.setText(title);
        binding.albumText.setText(albumName);
        binding.durationText.setText(duration);
        Glide.with(this).load(albumCoverUrl).into(binding.albumCover);

        binding.addFavorite.setOnClickListener(v -> {
            // Create a new Song object with the details
            final Song song = new Song(title, duration, albumName, albumCoverUrl);

            // Insert the song into the database using an executor
            executor.execute(() -> {
                db.songDao().insert(song);
                // If you need to do something on the UI thread after inserting, place it here
                runOnUiThread(() -> {
                    Toast.makeText(DeezerSongDetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
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
}
