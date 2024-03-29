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
public class SongFav extends AppCompatActivity {
    private RecyclerView favSongList;
    private List<Song> songs = new ArrayList<>();
    private SongDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);

        return true;
    }
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_title);
        builder.setMessage(R.string.help_message);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void fetchSongsFromDatabase(final RecyclerView.Adapter<SongViewHolder> adapter) {
        executor.execute(() -> {
            songs = db.songDao().getAll();
            runOnUiThread(adapter::notifyDataSetChanged);
        });
    }


    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(android.R.id.text1);
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