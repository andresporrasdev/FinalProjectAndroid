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

public class SavedRecipesActivity extends AppCompatActivity {

    private ActivitySavedRecipesBinding binding;
    private RecipeDAO recipeDao;
    private List<Recipe> savedRecipes = new ArrayList<>();
    private RecyclerView.Adapter mySavedRecipeAdapter;

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

    private void setupRecyclerView() {
        binding.recyclerViewSavedRecipes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewSavedRecipes.setAdapter(mySavedRecipeAdapter);
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedRecipes();
    }

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