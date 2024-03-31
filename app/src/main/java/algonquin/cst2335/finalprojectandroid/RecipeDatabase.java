package algonquin.cst2335.finalprojectandroid;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * File name: RecipeDatabase.java
 * Author: Tsaichun Chang
 * Course: CST2335-022
 * Assignment: Final Project
 * Date: 2024-03-29
 *
 * @author Tsaichun Chang
 * @version 1
 *
 * The Room database class for the application, providing the main access point to the persisted data.
 * This class includes methods to get the database instance and access the Data Access Objects (DAOs) for the entity {@link Recipe}.
 * It is annotated with {@link Database}, indicating that it's a Room database and specifying the entity classes and database version.
 *
 */
@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {

    /**
     * Returns the DAO (Data Access Object) for accessing the {@link Recipe} table.
     * @return The DAO for {@link Recipe} entities.
     */
    public abstract RecipeDAO recipeDAO();

    /**
     * The volatile instance of the database, ensuring atomic access to the database instance.
     */
    private static volatile RecipeDatabase INSTANCE;

    /**
     * Gets the singleton instance of the {@link RecipeDatabase}. If the instance doesn't exist,
     * it initializes it using the Room database builder to create a new database instance.
     * This method ensures that only one instance of the database is created and used throughout the application.
     *
     * @param context The context used to get the application context for initializing the database.
     * @return The single instance of {@link RecipeDatabase}.
     */
    public static RecipeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, "recipe_database").build();
                }
            }
        }
        return INSTANCE;
    }

}
