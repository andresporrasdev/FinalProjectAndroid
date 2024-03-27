package algonquin.cst2335.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
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

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Recipe> recipes = recipeDao.getAllRecipes();
            runOnUiThread(()-> {
                savedRecipes.clear();
                savedRecipes.addAll(recipes);
                mySavedRecipeAdapter.notifyDataSetChanged();
            });
        });

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
                        itemView.getContext().startActivity(intent);
                    }
                }
            });

        }

        void bind(Recipe recipe) {
            String imageUrl = recipe.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(binding.imageViewSavedRecipe.getContext())
                        .load(imageUrl)
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

    @Override
    protected void onResume() {
        super.onResume();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Recipe> recipes = recipeDao.getAllRecipes();
            runOnUiThread(() -> {
                savedRecipes.clear();
                savedRecipes.addAll(recipes);
                mySavedRecipeAdapter.notifyDataSetChanged();
            });
        });
    }


}