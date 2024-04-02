package algonquin.cst2335.finalprojectandroid.dictionary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.R;

public class FavoriteWords extends AppCompatActivity {

    private RecyclerView wordRecycler;
    private WordAdapter wordAdapter = new WordAdapter(new ArrayList<>());
    private DictionaryDatabase db;
    private ArrayList<DictionaryItem> wordList = new ArrayList<>();
    private DictionaryViewModel wordModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_words);

        wordRecycler = findViewById(R.id.wordRecycler);
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Fetch saved words from the database
        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionaryDatabase").build();
        DictionaryItemDAO dDAO = db.dictionaryItemDAO();

//        if (wordList.isEmpty()) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<DictionaryItem> allWords = dDAO.getAllWords();
                runOnUiThread(() -> {
                    wordList.addAll(allWords);
                        wordAdapter = new WordAdapter(wordList);
                        wordRecycler.setAdapter(wordAdapter);

                });
            });




        }

    private class WordAdapter extends RecyclerView.Adapter<WordViewHolder> {
        private final List<DictionaryItem> wordTermList;

        /**
         * Called by RecyclerView when it needs a new ViewHolder of the given type to represent an item.
         * @param wordTermList   The ViewGroup into which the new View will be added after it is bound to an adapter position
         * @return A new ViewHolder that holds a View of the given view type
         */
        public WordAdapter(List<DictionaryItem> wordTermList) {
            this.wordTermList = wordTermList;
        }

        /**
         * Called by RecyclerView when it needs a new ViewHolder of the given type to represent an item.
         * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position
         * @param viewType The type of the new View
         * @return A new ViewHolder that holds a View of the given view type
         */
        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            FragmentWordBinding wordBinding = FragmentWordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
//            return new WordViewHolder(wordBinding.getRoot());
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_favorite_words, parent, false);
            return new WordViewHolder(view);
        }

        /**
         * Called by RecyclerView to display the data at the specified position.
         * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position
         * @param position The position of the item within the adapter's data set
         */
        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            DictionaryItem wordEntity = wordTermList.get(position);
            holder.termTextView.setText(wordEntity.getWord());

            androidx.appcompat.widget.Toolbar toolbar = holder.wordInfo;

            toolbar.inflateMenu(R.menu.fave_word);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete_word) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage("Do you want to delete this Definition from your Favourites?")
                            .setTitle("Delete")
                            .setNegativeButton("No", (dialog, which) -> {
                                // User chose not to delete
                            })
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // User chose to delete


                                Executor thread1 = Executors.newSingleThreadExecutor();
                                thread1.execute(() -> {
                                    try {
                                        // Ensure that the deleteWordDefinition method is correctly implemented
                                        dDAO.deleteWord(word.getWord());
                                        Log.d("Deleted", "Rows affected: " + word.getWord());
                                    } catch (Exception e) {
                                        Log.e("DeleteError", "Error Deleting definition", e);
                                    }
                                });

                                // Log to check if the definition is being deleted
                                Log.d("definition", "definition deleted: " + word);

                                Snackbar.make(requireView(), "Definition deleted", Snackbar.LENGTH_LONG)
                                        .setAction("Undo", (btn) -> {
                                            // User clicked Undo
                                            Executor thread2 = Executors.newSingleThreadExecutor();
                                            thread2.execute(() -> {
                                                // undo the deletion in the database
                                                dDAO.insertItemDefinition(word);
                                            });
                                            adapter.notifyDataSetChanged();
                                        })
                                        .show();
                            });
                    builder.create().show();
                    Log.e("WordAdapter", "WordEntity is null");

                    return true;
                } else {
                    Log.e("WordAdapter", "WordEntity is null");
                    return false;
                }
            });
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         * @return The total number of items in this adapter's data set
         */
        @Override
        public int getItemCount() {
            return wordTermList.size();
        }
    }

    /**
     * ViewHolder class for displaying saved words and managing options in RecyclerView items.
     */
    private static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView termTextView;
        androidx.appcompat.widget.Toolbar wordInfo;

        /**
         * Constructs a new WordViewHolder.
         * @param itemView The View representing an item in the RecyclerView
         */
        public WordViewHolder(View itemView) {
            super(itemView);
            termTextView = itemView.findViewById(R.id.recyclerWordView);
            wordInfo = itemView.findViewById(R.id.details);
        }
    }

}
