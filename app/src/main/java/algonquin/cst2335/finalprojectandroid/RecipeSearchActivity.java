package algonquin.cst2335.finalprojectandroid;

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

public class RecipeSearchActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ActivityRecipeSearchBinding binding;
    private RecyclerView.Adapter myRecipeAdapter;
    private final String MY_KEY = "4e00cca874f74f3f9832355559576c8e";
    private final String URL_REQUEST_DATA = "https://api.spoonacular.com/recipes/complexSearch?query=";
    protected RequestQueue queue;
    protected String recipeName;
    ArrayList<Recipe> recipes = new ArrayList<>();

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

    private void saveSearchTerm(String searchTerm) {
        SharedPreferences sharedPreferences = getSharedPreferences("RecipePreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastSearchTerm", searchTerm);
        editor.apply();
    }

    private void retrieveLastSearchTerm() {
        SharedPreferences sharedPreferences = getSharedPreferences("RecipePreferences", MODE_PRIVATE);
        String lastSearchTerm = sharedPreferences.getString("lastSearchTerm", "");
        binding.recipeSearchEditText.setText(lastSearchTerm);
    }

    private void setupRecyclerView () {
        binding.recyclerViewRecipeList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewRecipeList.setAdapter(myRecipeAdapter);
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_rmenu, menu);
        return true;
    }

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

    public void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.guide_title))
                .setMessage(getString(R.string.guide_message))
                .setPositiveButton("OK", (dialog, ck) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}