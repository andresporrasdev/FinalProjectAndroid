package algonquin.cst2335.finalprojectandroid.dictionary;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for performing CRUD operations on the DictionaryItem entity in the database.
 */
@Dao
public interface DictionaryItemDAO {
    /**
     * Inserts a new dictionary item definition into the database.
     * @param wordDefinition The dictionary item to be inserted.
     */
    @Insert
    void insertItemDefinition(DictionaryItem wordDefinition);

    /**
     * Deletes a dictionary item definition from the database.
     * @param word The dictionary item to be deleted.
     */
    @Delete
    void deleteItemDefinition(DictionaryItem word);

    /**
     * Retrieves all dictionary items from the database.
     * @return A list of all dictionary items stored in the database.
     */
    @Query("Select * from word_definitions")
    List<DictionaryItem> getAllWords();

    /**
     * Deletes a dictionary item from the database based on its word.
     * @param word The word associated with the dictionary item to be deleted.
     */
    @Query("DELETE FROM word_definitions WHERE word = :word")
    void deleteWord(String word);
}
