/*
 * FileName: SongDao.java
 * Purpose: Data Access Object (DAO) for the {@link Song} entity.
 * Author: Jiaxin Yan
 * Lab Section: 022
 * Creation Date: 03/28/2024
 */
package algonquin.cst2335.finalprojectandroid;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * Data Access Object (DAO) for the {@link Song} entity. This interface provides the methods that
 * the rest of the app uses to interact with data in the song table of the database.
 * Room uses this DAO to create a clean API for the application. Each method annotated with a database
 * operation annotation represents a single atomic operation. The Room persistence library automatically
 * generates the necessary code for these operations.
 *
 * @author Jiaxin Yan
 * @lab_section 022
 * @creation_date 03/28/2024
 */
@Dao
    public interface SongDao {
        /**
         * Retrieves all songs from the database.
         *
         * @return A list of {@link Song} entities. If no songs are found, this list will be empty.
         */
        @Query("SELECT * FROM song")
        List<Song> getAll();
        /**
         * Finds a song in the database by its ID.
         *
         * @param id The unique identifier of the song.
         * @return The {@link Song} entity with the specified ID or null if no song is found.
         */
        @Query("SELECT * FROM song WHERE id = :id")
        Song findById(int id);
        /**
         * Inserts a song into the database. If the song already exists, it is replaced.
         *
         * @param song The {@link Song} entity to be inserted.
         */
        @Insert
        void insert(Song song);
        /**
         * Deletes a specific song from the database.
         *
         * @param song The {@link Song} entity to be deleted.
         */
        @Delete
        void delete(Song song);


    }
