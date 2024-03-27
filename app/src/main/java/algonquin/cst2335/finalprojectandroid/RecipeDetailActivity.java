package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityRecipeDetailBinding;

public class RecipeDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final String MY_KEY = "774f605053f045abad38658ffe65170b";
    private ActivityRecipeDetailBinding binding;
    protected RequestQueue queue;
    private String imageUrl;
    private String summary;
    private String sourceUrl;
    private RecipeDAO recipeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecipeDatabase db = RecipeDatabase.getDatabase(getApplicationContext());
        recipeDAO = db.recipeDAO();

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Log.e(TAG, "Recipe ID is missing");
            Toast.makeText(this, "Error: Recipe ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String title = getIntent().getStringExtra("RECIPE_TITLE");


        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + MY_KEY;
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                (response) -> {
                    try {
                        imageUrl = response.getString("image");
                        summary = response.getString("summary");
                        sourceUrl = response.getString("sourceUrl");

                        Glide.with(this).load(imageUrl).into(binding.detailImageViewRecipe);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            binding.detailTextViewRecipeSummary.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            binding.detailTextViewRecipeSummary.setText(Html.fromHtml(summary));
                        }

                        binding.urlDescription.setText(sourceUrl);
                        binding.urlDescription.setOnClickListener(view -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl));
                            startActivity(browserIntent);
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(this, "Error fetching recipe details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.e(TAG, "Error: " + error.getMessage()));
                queue.add(request);

        binding.buttonSaveRecipe.setOnClickListener(view -> {
            Recipe recipe = new Recipe(recipeId, title, imageUrl, summary, sourceUrl);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                recipeDAO.insert(recipe);
                runOnUiThread(() -> Toast.makeText(RecipeDetailActivity.this, "Recipe Saved!", Toast.LENGTH_SHORT).show());
            });
        });
    }
}