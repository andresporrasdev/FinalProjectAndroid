package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityRecipeDetailBinding;

/**
 * File name: RecipeDetailActivity.java
 * Author: Tsaichun Chang
 * Course: CST2335-022
 * Assignment: Final Project
 * Date: 2024-03-29
 *
 * @author Tsaichun Chang
 * @version 1
 *
 * It retrieves recipe details from an external API using the recipe ID passed through an intent.
 * Users can view the recipe's image, summary, and source URL, and have the option to save the recipe
 * to a local database for future reference.
 *
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final String MY_KEY = "4e00cca874f74f3f9832355559576c8e";
    private ActivityRecipeDetailBinding binding;
    protected RequestQueue queue;
    private String imageUrl;
    private String summary;
    private String sourceUrl;
    private RecipeDAO recipeDAO;

    /**
     * Initializes the activity, sets up view binding, retrieves the recipe ID passed through the intent,
     * and makes a network request to fetch the recipe's details. It also initializes the database access object
     * and sets up the UI to display the recipe's information. Users can save the recipe to the local database.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
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
            Toast.makeText(this, getString(R.string.id_err), Toast.LENGTH_SHORT).show();
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

                        Glide.with(this)
                             .load(imageUrl)
                             .transform(new CenterCrop(), new RoundedCorners(30))
                             .into(binding.detailImageViewRecipe);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            binding.detailTextViewRecipeSummary.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            binding.detailTextViewRecipeSummary.setText(Html.fromHtml(summary));
                        }

                        binding.detailTextViewRecipeTitle.setText(title);

                        binding.urlDescription.setText(sourceUrl);
                        binding.urlDescription.setOnClickListener(clk -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl));
                            startActivity(browserIntent);
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(this, getString(R.string.fc_err), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.e(TAG, "Error: " + error.getMessage()));
                queue.add(request);

        binding.buttonSaveRecipe.setOnClickListener(view -> {
            Recipe recipe = new Recipe(recipeId, title, imageUrl, summary, sourceUrl);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                recipeDAO.insert(recipe);
                runOnUiThread(() -> Snackbar.make(view, getString(R.string.save_recipe), Toast.LENGTH_LONG).show());
            });
        });
    }
}