package algonquin.cst2335.finalprojectandroid.dictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.R;

public class FavoriteWords extends AppCompatActivity {

    private RecyclerView wordRecycler;
    private WordAdapter wordAdapter;
    private DictionaryDatabase db;
    private ArrayList<DictionaryItem> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_words);

        wordRecycler = findViewById(R.id.wordRecycler);
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionaryDatabase").build();

        // Fetch saved words from the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<DictionaryItem> allWords = db.dictionaryItemDAO().getAllWords();
            runOnUiThread(() -> {
                wordList.addAll(allWords);
                wordAdapter.notifyDataSetChanged();
            });
        });

        wordAdapter = new WordAdapter(wordList);
        wordRecycler.setAdapter(wordAdapter);
    }

    private class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {
        private final List<DictionaryItem> wordTermList;

        public WordAdapter(List<DictionaryItem> wordTermList) {
            this.wordTermList = wordTermList;
        }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_word_item, parent, false);
            return new WordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            DictionaryItem wordEntity = wordTermList.get(position);
            holder.termTextView.setText(wordEntity.getWord());

            holder.itemView.setOnClickListener(v -> {
                // Check if the position is valid
                if (position >= 0 && position < wordTermList.size()) {
                    // Get the clicked item
                    DictionaryItem clickedItem = wordTermList.get(position);

                    // Perform any action here, for example, display details of the clicked item
                    Toast.makeText(v.getContext(), "Clicked item: " + clickedItem.getWord(), Toast.LENGTH_SHORT).show();
                } else {
                    // Log a message or handle the case where the position is not valid
                    Log.e("ViewHolder", "Invalid position: " + position);
                }
            });

            holder.itemView.setOnLongClickListener(v -> {
                Context context = holder.itemView.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Do you want to delete this word from your favorites?")
                        .setTitle("Delete")
                        .setNegativeButton("No", (dialog, which) -> {
                            // Do nothing on cancel
                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Execute delete operation asynchronously using Room database
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() -> {
                                // Perform delete operation
                                db.dictionaryItemDAO().deleteItemDefinition(wordEntity);

                                // Update UI on the main thread
                                runOnUiThread(() -> {
                                    // Remove the item from the list and notify the adapter
                                    wordTermList.remove(position);
                                    notifyItemRemoved(position);

                                    // Show a snackbar indicating the word is deleted
                                    Snackbar.make(holder.itemView, "Word deleted from favorites", Snackbar.LENGTH_LONG).show();
                                });
                            });
                        }).create().show();
                return true;
            });

        }

        @Override
        public int getItemCount() {
            return wordTermList.size();
        }
    }

    private static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView termTextView;

        public WordViewHolder(View itemView) {
            super(itemView);
            termTextView = itemView.findViewById(R.id.recyclerWordView);
        }
    }
}
