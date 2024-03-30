package algonquin.cst2335.finalprojectandroid.dictionary;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import algonquin.cst2335.finalprojectandroid.R;

import android.app.AlertDialog;
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


public class Dictionary extends AppCompatActivity {

    private DictionaryDatabase db;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private Button searchButton;
    private Button viewSavedButton;
    DictionaryViewModel viewModel;
    private ArrayList<DictionaryItem> wordDefinitionsList = new ArrayList<>();
    private ArrayList<DictionaryItem> favsList = new ArrayList<>();
    androidx.appcompat.widget.Toolbar toolbar;
    RequestQueue queue;

//    private WordDefinitionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dictionary);
        queue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        viewSavedButton = findViewById(R.id.savedDefinitions);
        toolbar = findViewById(R.id.toolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        adapter = new WordDefinitionAdapter(wordDefinitionsList);
//        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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


} // end class