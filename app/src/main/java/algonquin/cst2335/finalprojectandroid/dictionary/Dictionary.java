package algonquin.cst2335.finalprojectandroid.dictionary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.R;

public class Dictionary extends AppCompatActivity {

    private DictionaryDatabase db;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton;
    private Button viewSavedButton;
    private ArrayList<DictionaryItem> wordDefinitionsList = new ArrayList<>();
    private ArrayList<DictionaryItem> favsList = new ArrayList<>();
    androidx.appcompat.widget.Toolbar toolbar;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        viewSavedButton = findViewById(R.id.savedDefinitions);
        toolbar = findViewById(R.id.toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        WordDefinitionAdapter adapter = new WordDefinitionAdapter(wordDefinitionsList);
        recyclerView.setAdapter(adapter);

        // Initialize the database
        db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionaryDatabase").build();

        viewSavedButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoriteWords.class);
            startActivity(intent);
        });

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchEditText.getText().toString().trim();
            if (!searchTerm.isEmpty()) {
                saveSearchedWord(searchTerm);
                String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + searchTerm;

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                        response -> handleResponse(response, searchTerm),
                        error -> Toast.makeText(Dictionary.this, "Error: The word searched can not be found", Toast.LENGTH_SHORT).show());
                queue.add(jsonArrayRequest);
            } else {
                Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleResponse(JSONArray response, String searchTerm) {
        try {
            wordDefinitionsList.clear(); // Clear the existing list

            for (int i = 0; i < response.length(); i++) {
                JSONObject wordObject = response.getJSONObject(i);
                JSONArray meanings = wordObject.getJSONArray("meanings");

                StringBuilder definitionsString = new StringBuilder();

                for (int j = 0; j < meanings.length(); j++) {
                    JSONObject meaning = meanings.getJSONObject(j);
                    JSONArray definitionsArray = meaning.getJSONArray("definitions");

                    for (int k = 0; k < definitionsArray.length(); k++) {
                        JSONObject definitionObject = definitionsArray.getJSONObject(k);
                        String definition = definitionObject.getString("definition");
                        definitionsString.append(definition).append("\n");
                    }
                }
                wordDefinitionsList.add(new DictionaryItem(searchTerm, definitionsString.toString()));
            }

            // Update RecyclerView

            WordDefinitionAdapter adapter = new WordDefinitionAdapter(wordDefinitionsList);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Dictionary.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    public class WordDefinitionAdapter extends RecyclerView.Adapter<WordDefinitionAdapter.ViewHolder> {
        private List<DictionaryItem> wordDefinitionsList;

        public WordDefinitionAdapter(List<DictionaryItem> wordDefinitionsList) {
            this.wordDefinitionsList = wordDefinitionsList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView definitionTextView;
            androidx.appcompat.widget.Toolbar toolbar;

            public ViewHolder(View itemView) {
                super(itemView);
                definitionTextView = itemView.findViewById(R.id.definitionTextView);
                toolbar = itemView.findViewById(R.id.definitionsToolbar);
            }

            public void bind(String definition) {
                definitionTextView.setText(definition);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            WordDefinitionAdapter adapter = new WordDefinitionAdapter(wordDefinitionsList);
            recyclerView.setAdapter(adapter);

            String definition = wordDefinitionsList.get(position).getDefinitions();
            holder.bind(definition);

            db = Room.databaseBuilder(Dictionary.this, DictionaryDatabase.class, "dictionaryDatabase").build();
            DictionaryItemDAO dDAO = db.dictionaryItemDAO();
//            androidx.appcompat.widget.Toolbar toolbar = holder.definitionsMenu;
//            toolbar.inflateMenu(R.menu.definitions_menu);

            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.save) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Dictionary.this);
                    builder.setMessage("Do you want to add this Definition to your Favorites?")
                            .setTitle("Add")
                            .setNegativeButton("No", (dialog, which) -> {
                            })
                            .setPositiveButton("Yes", (dialog, which) -> {

                                DictionaryItem definitionToAdd = wordDefinitionsList.get(position);
                                favsList.add(definitionToAdd);

                                if (definitionToAdd != null) {
                                    Executor thread1 = Executors.newSingleThreadExecutor();
                                    thread1.execute(() -> {
                                        try {
                                            // Ensure that the insertItemDefinition method is correctly implemented
                                            dDAO.insertItemDefinition(definitionToAdd);
                                            Log.d("InsertResult", "Rows affected: " + definitionToAdd);
                                        } catch (Exception e) {
                                            Log.e("InsertError", "Error inserting definition", e);
                                        }
                                    });

                                    // Log to check if the definition is being added to the list
                                    Log.d("Definition", "Definition to add: " + definitionToAdd);

                                    Snackbar.make(findViewById(android.R.id.content), "Definition added to favourites", Snackbar.LENGTH_LONG)
                                            .setAction("Undo", (btn) -> {
                                                Executor thread2 = Executors.newSingleThreadExecutor();
                                                thread2.execute(() -> {
                                                    // undo the addition from the database
                                                    dDAO.deleteItemDefinition(definitionToAdd);
                                                });
                                                // undo the addition from the list
                                                wordDefinitionsList.remove(definitionToAdd);
                                                // Notify adapter of the data change
                                                adapter.notifyItemRemoved(position);
                                            }).show();
                                }
                            });
                    builder.create().show();
                }
                if (item.getItemId() == R.id.help){
                    showHelpInformation();
                }
                return false;
            });
        }
        @Override
        public int getItemCount() {
            return wordDefinitionsList.size();
        }
    }

    private void saveSearchedWord(String searchTerm) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("last_searched_word", searchTerm);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.definitions_menu, menu);
        return true;
    }

    private void showHelpInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help Information");

        // Display help information in a dialog
        String helpText = "Enter a word into the search bar. Then press search. A list of definitions " +
                "will appear below the search bar. To save a definition, press the icon to the right of the definition. A message " +
                "will show up at the bottom of your screen if you want to undo, click undo if you would like to undo the save." +
                " If you want to view your saved words, click the button at the bottom of the page that says View Saved Terms." +
                " It will bring you to a new page that shows all of the words you saved. You can then scroll through your words and " +
                "click the 'i' icon to the right of the word to view the definitions of all of the words. When in the view page, you" +
                " can click on the delete icon on the right of the definition if you want to delete the definition. To go back press " +
                "the back button that is apart of your device. ";
        builder.setMessage(helpText);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.help) {
            // Show help information in a dialog
            showHelpInformation();
            return true;
        }
        return false;
    }
}
