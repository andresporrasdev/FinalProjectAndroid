/*
 * Purpose: Defines the Room database configuration for the Sunrise & Sunset Lookup App.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid.sslookup;
import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Represents the Room database for the app, containing the table for location entities and providing
 * access to the data access object (DAO) for these entities.
 */
@Database(entities = {LocationEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Gets the Location DAO (Data Access Object) associated with this database.
     *
     * @return The DAO for accessing location data.
     */
    public abstract LocationDao locationDao();
}

