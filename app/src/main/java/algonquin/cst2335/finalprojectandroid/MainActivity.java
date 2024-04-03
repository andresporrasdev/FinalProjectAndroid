package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import algonquin.cst2335.finalprojectandroid.dictionary.Dictionary;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("All-In-One Hub");
        setSupportActionBar(toolbar);

        Button btnRecipeSearch = findViewById(R.id.btnRecipeSearch);
        Button btnDictionary = findViewById(R.id.btnDictionary);
        Button btnSunriseSunsetlookup = findViewById(R.id.btnSunriseSunsetlookup);
        btnSunriseSunsetlookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SSLookupActivity.class);
                startActivity(intent);
            }
        });
        Button btnDeezerSongSearch = findViewById(R.id.btnDeezerSongSearch);



        btnRecipeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
                startActivity(intent);
            }
        });
        btnDeezerSongSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeezerSongActivity.class);
                startActivity(intent);
            }
        });
        btnDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Dictionary.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sun) {
            Intent intent = new Intent(MainActivity.this, SSLookupActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_recipe) {
            Intent intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dictionary) {
            Intent intent = new Intent(MainActivity.this, Dictionary.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_album) {
            // Perform action for the album or navigate
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}