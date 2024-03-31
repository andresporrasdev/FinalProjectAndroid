package algonquin.cst2335.finalprojectandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectandroid.databinding.ActivitySavedRecipeListBinding;
import algonquin.cst2335.finalprojectandroid.databinding.ActivitySavedRecipesBinding;

/**
 * File name: SavedRecipesActivity.java
 * Author: Tsaichun Chang
 * Course: CST2335-022
 * Assignment: Final Project
 * Date: 2024-03-29
 *
 * @author Tsaichun Chang
 * @version 1
 *
 *  Activity class for displaying and managing a list of saved recipes.
 *  This class is responsible for initializing and setting up the RecyclerView
 *  to display saved recipes, handling click events to view and delete recipes,
 *  and managing the data flow between the UI and the database.
 */
public class SavedRecipesActivity extends AppCompatActivity {

    private ActivitySavedRecipesBinding binding;
    private RecipeDAO recipeDao;
    private List<Recipe> savedRecipes = new ArrayList<>();
    private RecyclerView.Adapter mySavedRecipeAdapter;

    /**
     * Initializes the activity, sets up the RecyclerView and its adapter,
     * and loads the saved recipes from the database.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle
     *                           contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecipeDatabase db = RecipeDatabase.getDatabase(getApplicationContext());
        recipeDao = db.recipeDAO();

        setupRecyclerView();
        loadSavedRecipes();

        binding.recyclerViewSavedRecipes.setAdapter(mySavedRecipeAdapter = new RecyclerView.Adapter<MySavedRecipeHolder>() {
            @NonNull
            @Override
            public MySavedRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                ActivitySavedRecipeListBinding itemBinding = ActivitySavedRecipeListBinding.inflate(layoutInflater, parent, false);
                return new MySavedRecipeHolder(itemBinding);
            }
            @Override
            public void onBindViewHolder(@NonNull MySavedRecipeHolder holder, int position) {
                Recipe recipe = savedRecipes.get(position);
                holder.bind(recipe);
            }
            @Override
            public int getItemCount() {
                return savedRecipes.size();
            }
        });
    }

    /**
     * Inner class for providing a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * the holder provides access to all the views for a data item in a recycler view.
     */
    class MySavedRecipeHolder extends RecyclerView.ViewHolder {
        private ActivitySavedRecipeListBinding binding;
        public MySavedRecipeHolder(ActivitySavedRecipeListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Recipe recipe = savedRecipes.get(position);
                        Intent intent = new Intent(v.getContext(), SavedRecipesDetailActivity.class);
                        intent.putExtra("RECIPE_ID", recipe.getRecipeId());
                        intent.putExtra("RECIPE_TITLE", recipe.getTitle());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Recipe recipe = savedRecipes.get(position);
                        showDeleteConfirmationDialog(recipe.getRecipeId());
                        return true;
                    }
                    return false;
                }
            });
        }

        void bind(Recipe recipe) {
            String imageUrl = recipe.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(binding.imageViewSavedRecipe.getContext())
                        .load(imageUrl)
                        .transform(new CenterCrop(), new RoundedCorners(50))
                        .into(binding.imageViewSavedRecipe);
            } else {
                binding.imageViewSavedRecipe.setImageResource(R.drawable.default_image);
            }
            binding.textViewSavedRecipeTitle.setText(recipe.getTitle());
        }

    }

    /**
     * Sets up the RecyclerView including setting its layout manager.
     */
    private void setupRecyclerView() {
        binding.recyclerViewSavedRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSavedRecipes.setAdapter(mySavedRecipeAdapter);
    }

    /**
     * Shows a confirmation dialog to delete a recipe. If confirmed, it
     * deletes the recipe from the database and updates the UI accordingly.
     *
     * @param RecipeId The ID of the recipe to be deleted.
     */
    private void showDeleteConfirmationDialog(int RecipeId) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_recipe_title))
                .setMessage(getString(R.string.delete_recipe_message))
                .setPositiveButton(getString(R.string.delete_confirm), (dialog, clk) -> {
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(()-> {
                        recipeDao.deleteByRecipeID(RecipeId);
                        runOnUiThread(() -> {
                            Toast.makeText(SavedRecipesActivity.this, getString(R.string.recipe_deleted_successfully), Toast.LENGTH_SHORT).show();
                            loadSavedRecipes();
                        });
                    });
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Reloads the saved recipes from the database into the RecyclerView
     * whenever the activity resumes, ensuring the data displayed is up-to-date.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadSavedRecipes();
    }

    /**
     * Loads the saved recipes from the database asynchronously, updates the
     * UI thread with the fetched data, and refreshes the RecyclerView to
     * display the latest data.
     */
    private void loadSavedRecipes() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Recipe> recipes = recipeDao.getAllRecipes();
            runOnUiThread(() -> {
                savedRecipes.clear();
                savedRecipes.addAll(recipes);
                mySavedRecipeAdapter.notifyDataSetChanged();
                if (recipes.isEmpty()) {
                    Snackbar.make(binding.recyclerViewSavedRecipes, getString(R.string.no_recipe), Snackbar.LENGTH_LONG).show();
                }
            });
        });
    }

}