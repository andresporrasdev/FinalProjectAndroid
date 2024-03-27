package algonquin.cst2335.finalprojectandroid;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDAO {

    @Insert
    public long insert(Recipe recipe);

    @Query("DELETE FROM recipes WHERE recipe_id = :recipeId")
    public void deleteByRecipeID(String recipeId);

    @Query("SELECT * FROM recipes")
    public List<Recipe> getAllRecipes();

}
