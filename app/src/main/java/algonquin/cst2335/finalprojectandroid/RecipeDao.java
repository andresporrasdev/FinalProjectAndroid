package algonquin.cst2335.finalprojectandroid;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insert(Recipe recipe);

    @Query("DELETE FROM recipes WHERE recipe_id = :recipeId")
    void deleteByRecipeID(String recipeId);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAllRecipes();

}
