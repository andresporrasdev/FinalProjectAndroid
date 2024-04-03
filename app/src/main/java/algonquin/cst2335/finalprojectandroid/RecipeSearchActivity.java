package algonquin.cst2335.finalprojectandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityRecipeListBinding;
import algonquin.cst2335.finalprojectandroid.databinding.ActivityRecipeSearchBinding;

/**
 * File name: RecipeSearchActivity.java
 * Author: Tsaichun Chang
 * Course: CST2335-022
 * Assignment: Final Project
 * Date: 2024-03-29
 *
 * @author Tsaichun Chang
 * @version 1
 *
 *  An {@link AppCompatActivity} that handles searching for recipes via an external API,
 *  * displaying search results, and navigating to recipe details.
 *  * This activity provides a user interface for entering a search term, initiating the search,
 *  * and viewing a list of recipes that match the search criteria. Users can select a recipe to view
 *  * its details or navigate to their saved recipes.
 */
public class RecipeSearchActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ActivityRecipeSearchBinding binding;
    private RecyclerView.Adapter myRecipeAdapter;
    private final String MY_KEY = "4e00cca874f74f3f9832355559576c8e";
    private final String URL_REQUEST_DATA = "https://api.spoonacular.com/recipes/complexSearch?query=";
    protected RequestQueue queue;
    protected String recipeName;
    ArrayList<Recipe> recipes = new ArrayList<>();

    /**
     * Initializes the activity, sets up the RecyclerView for displaying search results,
     * and configures the search button to initiate searches. Previous search terms are retrieved
     * and displayed in the search field.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrieveLastSearchTerm();

        queue = Volley.newRequestQueue(this);

        setupRecyclerView();

        setSupportActionBar(binding.myToolbar);

        binding.searchButton.setOnClickListener( click -> {
            recipeName = binding.recipeSearchEditText.getText().toString();

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            saveSearchTerm(recipeName);
            try {
                if (!recipeName.isEmpty()) {
                    String url = URL_REQUEST_DATA + recipeName + "&apiKey=" + MY_KEY;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            (response) -> {
                                try {
                                    JSONArray resultsArray = response.getJSONArray("results");
                                    ArrayList<Recipe> fetchedRecipes = new ArrayList<>();

                                    for (int i = 0; i < resultsArray.length(); i++) {
                                        JSONObject recipeObject = resultsArray.getJSONObject(i);
                                        int id = recipeObject.getInt("id");
                                        String title = recipeObject.getString("title");
                                        String image = recipeObject.getString("image");

                                        fetchedRecipes.add(new Recipe(id, title, image));
                                    }

                                    runOnUiThread(() -> {
                                        recipes.clear();
                                        recipes.addAll(fetchedRecipes);
                                        myRecipeAdapter.notifyDataSetChanged();
                                    });

                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing JSON response", e);
                                    Toast.makeText(RecipeSearchActivity.this, getString(R.string.ps_err), Toast.LENGTH_SHORT).show();
                                }
                            },
                            (error) -> {
                                Log.e(TAG, "Error:" + error.getMessage());
                                Snackbar.make(click, getString(R.string.nt_err), Snackbar.LENGTH_SHORT).show();
                            });
                    queue.add(request);
                } else {
                    Snackbar.make(click, getString(R.string.et_err), Snackbar.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Log.e(TAG, getString(R.string.ec_err));
            }
        });

        binding.recyclerViewRecipeList.setAdapter(myRecipeAdapter = new RecyclerView.Adapter<MyRecipeHolder>() {
            @NonNull
            @Override
            public MyRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ActivityRecipeListBinding activityRecipeListBinding = ActivityRecipeListBinding.inflate(getLayoutInflater());
                return new MyRecipeHolder(activityRecipeListBinding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRecipeHolder holder, int position) {
                Recipe recipe = recipes.get(position);
                String imageUrl = recipe.getImage();
                Glide.with(holder.itemView.getContext())
                     .load(imageUrl)
                        .transform(new CenterCrop(), new RoundedCorners(50))
                        .into(holder.imageView);
                holder.titleText.setText(recipe.getTitle());
            }

            @Override
            public int getItemCount() {
                return recipes.size();
            }

        });

    }

    /**
     * Saves the current search term to SharedPreferences for retrieval on next app launch.
     *
     * @param searchTerm The search term to save.
     */
    private void saveSearchTerm(String searchTerm) {
        SharedPreferences sharedPreferences = getSharedPreferences("RecipePreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastSearchTerm", searchTerm);
        editor.apply();
    }

    /**
     * Retrieves the last search term from SharedPreferences and sets it in the search text field.
     */
    private void retrieveLastSearchTerm() {
        SharedPreferences sharedPreferences = getSharedPreferences("RecipePreferences", MODE_PRIVATE);
        String lastSearchTerm = sharedPreferences.getString("lastSearchTerm", "");
        binding.recipeSearchEditText.setText(lastSearchTerm);
    }

    /**
     * Sets up the RecyclerView, including specifying its layout manager and adapter.
     */
    private void setupRecyclerView () {
        binding.recyclerViewRecipeList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewRecipeList.setAdapter(myRecipeAdapter);
    }

    /**
     * Inner class for providing a reference to the views for each data item in the RecyclerView.
     * This holder class is used to display each recipe in the list of search results.
     */
    class MyRecipeHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;

        public MyRecipeHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewRecipe);
            titleText = itemView.findViewById(R.id.textViewRecipeTitle);

            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    Recipe recipe = recipes.get(position);
                    Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                    intent.putExtra("RECIPE_ID", recipe.getRecipeId());
                    intent.putExtra("RECIPE_TITLE", recipe.getTitle());
                    intent.putExtra("RECIPE_IMAGE_URL", recipe.getImage());
                    view.getContext().startActivity(intent);
                }
            });

        }
    }

    /**
     * Inflates the menu options from a menu resource (R.menu.my_rmenu).
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_rmenu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed,
     *         true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item1) {
            showInstructionsDialog();
        } else if (itemId == R.id.item2) {
            Intent intent = new Intent(this, SavedRecipesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays an AlertDialog with instructions on how to use the app.
     * The dialog contains a guide for searching and viewing recipes.
     */
    public void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.guide_title))
                .setMessage(getString(R.string.guide_message))
                .setPositiveButton("OK", (dialog, ck) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}