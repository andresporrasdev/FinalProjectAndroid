package algonquin.cst2335.finalprojectandroid.dictionary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * A Room Database class for managing the persistence of {@link DictionaryItem} entities.
 */
@Database(entities = {DictionaryItem.class}, version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {
    /**
     * Provides access to the Data Access Object (DAO) for {@link DictionaryItem}.
     * @return The Data Access Object (DAO) for {@link DictionaryItem}.
     */
    public abstract DictionaryItemDAO dictionaryItemDAO();
}
