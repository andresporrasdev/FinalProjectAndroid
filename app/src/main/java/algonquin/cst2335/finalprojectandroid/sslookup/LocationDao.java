/*
 * Purpose: Defines the data access methods for interacting with the location entities in the Room database.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid.sslookup;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * Data Access Object (DAO) for the location database. Provides methods for interacting
 * with the locations table within the app's database.
 */
@Dao
public interface LocationDao {

    /**
     * Inserts a new location into the database.
     *
     * @param location The location entity to be inserted.
     */
    @Insert
    void insertLocation(LocationEntity location);

    /**
     * Retrieves all locations stored in the database.
     *
     * @return A list of all location entities in the database.
     */
    @Query("SELECT * FROM locations")
    List<LocationEntity> getAllLocations();

    /**
     * Deletes a specified location from the database.
     *
     * @param location The location entity to be deleted.
     */
    @Delete
    void deleteLocation(LocationEntity location);

    /**
     * Retrieves a location by its ID from the database.
     *
     * @param id The ID of the location to retrieve.
     * @return The location entity with the specified ID.
     */
    @Query("SELECT * FROM locations WHERE id = :id")
    LocationEntity getLocationById(int id);

}

