package algonquin.cst2335.finalprojectandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import algonquin.cst2335.finalprojectandroid.databinding.ActivityRecipeSearchBinding;

public class RecipeSearchActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ActivityRecipeSearchBinding binding;
    private RecyclerView.Adapter myAdapter;
    private RecipeDao recipeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchButton.setOnClickListener( view -> {
            String query = binding.recipeSearchEditText.getText().toString();
            if (!query.isEmpty()) {
                searchRecipes(query);
            }
        });

    }

    private void searchRecipes(String query) {
        String apiKey = "774f605053f045abad38658ffe65170b";
        String url = "https://api.spoonacular.com/recipes/complexSearch?query=" + query + "&apiKey=" + apiKey;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        response -> {
                            Log.d(TAG, "Response: " + response.toString());
                            // Handle the JSON response here
                            // Update your UI with the list of recipes
                        },
                        error -> {

                        });

        requestQueue.add(jsonObjectRequest);

    }

//    binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<RecipeViewHolder>()) {
//
//    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView summaryText;
        ImageView recipeImage;

        public RecipeViewHolder(View itemView, int viewType) {
            super(itemView);



        }

    }

}