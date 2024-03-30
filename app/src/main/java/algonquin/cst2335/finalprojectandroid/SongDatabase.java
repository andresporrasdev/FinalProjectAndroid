/*
 * FileName: SongDatabase.java
 * Purpose:  Represents the database for the application, including all of its entities and version.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * Represents the database for the application, including all of its entities and version. This class
 * extends {@link RoomDatabase}, providing the main access point to the persisted data. Currently, it
 * includes the {@link Song} entity. Room will use this class to create the database tables for entities
 * and compile the DAOs containing the methods used for accessing the database.
 *
 * The {@code @Database} annotation specifies the entities included in the database and the version number.
 * Each entity corresponds to a table that will be created in the database. The version number is used to manage
 * migrations and ensure the database schema is up to date.
 *
 * This database class provides an abstract method for each DAO class that it uses. In this case, it provides
 * access to {@link SongDao}, which contains methods to interact with the {@code Song} table.
 */
@Database(entities = {Song.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {
    /**
     * Gets the DAO for accessing the {@link Song} table. Room implementation will automatically
     * generate the necessary code for this method when it is called.
     *
     * @return The DAO instance for interacting with the {@link Song} table.
     */
    public abstract SongDao songDao();
}