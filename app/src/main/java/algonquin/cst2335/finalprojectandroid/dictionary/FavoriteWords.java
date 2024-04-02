package algonquin.cst2335.finalprojectandroid.dictionary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

//        RecyclerView recyclerView = binding.defragRecyclerView;
//
        wordRecycler = findViewById(R.id.wordRecycler);
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));
//        wordRecycler.setAdapter(wordAdapter);

//        RecyclerView recyclerView = findViewById(R.id.wordRecycler);

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

//                    if (wordAdapter == null) {
//                        wordAdapter = new WordAdapter(wordList);
////                        wordRecycler.setAdapter(wordAdapter);
//                    } else {
//                        wordAdapter.notifyDataSetChanged();
//                    }
                });
            });
//        } // end if



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });    }

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_favorite_words, parent, false);
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
                if (item.getItemId() == R.id.infoButton) {
//                    try {
//                        DefinitionFragment wordDetailFragment = new DefinitionFragment(wordEntity);
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        transaction.addToBackStack("please");
//                        transaction.replace(R.id.frag, wordDetailFragment);
//                        transaction.commit();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
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
