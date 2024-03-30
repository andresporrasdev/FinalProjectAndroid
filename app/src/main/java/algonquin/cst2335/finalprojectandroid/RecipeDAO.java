package algonquin.cst2335.finalprojectandroid;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * File name: RecipeDAO.java
 * Author: Tsaichun Chang
 * Course: CST2335-022
 * Assignment: Final Project
 * Date: 2024-03-29
 *
 * @author Tsaichun Chang
 * @version 1
 *
 *
 * Data Access Object (DAO) for managing CRUD operations for {@link Recipe} entities.
 * This interface defines methods for inserting, deleting, and querying {@link Recipe} objects from the database.
 * It is annotated with {@link Dao}, indicating it's a DAO managed by Room.
 */
@Dao
public interface RecipeDAO {

    /**
     * Inserts a new {@link Recipe} into the database.
     *
     * @param recipe The {@link Recipe} object to be inserted.
     * @return The row ID of the newly inserted {@link Recipe}, as a {@code long}.
     */
    @Insert
    public long insert(Recipe recipe);

    /**
     * Deletes a {@link Recipe} from the database based on its ID.
     *
     * @param recipeId The unique ID of the {@link Recipe} to be deleted.
     */
    @Query("DELETE FROM recipes WHERE recipe_id = :recipeId")
    public void deleteByRecipeID(int recipeId);

    /**
     * Retrieves all {@link Recipe} entities from the database.
     *
     * @return A {@link List} of {@link Recipe} objects.
     */
    @Query("SELECT * FROM recipes")
    public List<Recipe> getAllRecipes();

}
