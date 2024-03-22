package algonquin.cst2335.finalprojectandroid;

import androidx.room.Database;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase {

    public abstract RecipeDao recipeDAO();

}
