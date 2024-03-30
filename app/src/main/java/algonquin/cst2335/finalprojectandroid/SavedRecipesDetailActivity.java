package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import algonquin.cst2335.finalprojectandroid.databinding.ActivitySavedRecipesDetailBinding;

public class SavedRecipesDetailActivity extends AppCompatActivity {

    private ActivitySavedRecipesDetailBinding binding;
    private RecipeDAO recipeDAO;
    private final String TAG = getClass().getSimpleName();
    private final String MY_KEY = "4e00cca874f74f3f9832355559576c8e";
    protected RequestQueue queue;
    private String imageUrl;
    private String summary;
    private String sourceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedRecipesDetailBinding.inflate(getLayoutInflater());
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
                                .into(binding.SavedDetailImageViewRecipe);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            binding.SavedDetailTextViewRecipeSummary.setText(Html.fromHtml(summary, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            binding.SavedDetailTextViewRecipeSummary.setText(Html.fromHtml(summary));
                        }

                        binding.SavedDetailTextViewRecipeTitle.setText(title);
                        binding.SavedUrlDescription.setText(sourceUrl);
                        binding.SavedUrlDescription.setOnClickListener(clk -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl));
                            startActivity(browserIntent);
                        });

                    } catch ( Exception e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        Toast.makeText(this, getString(R.string.fc_err), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Log.e(TAG, "Error: " + error.getMessage()));
                queue.add(request);

                binding.buttonDeleteRecipe.setOnClickListener(click -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SavedRecipesDetailActivity.this);
                    builder.setTitle(getString(R.string.delete_recipe_title))
                            .setMessage(R.string.delete_recipe_message)
                            .setNegativeButton(getString(R.string.delete_no), (dialog, cl) -> {})
                            .setPositiveButton(getString(R.string.delete_yes), (dialog, cli) -> {
                                Executor excecutor = Executors.newSingleThreadExecutor();
                                excecutor.execute(() -> {
                                    recipeDAO.deleteByRecipeID(recipeId);
                                    runOnUiThread(() -> {
                                        Snackbar.make(click, getString(R.string.recipe_deleted_successfully), Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                                });
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });
    }
}