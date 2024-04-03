package algonquin.cst2335.finalprojectandroid.dictionary;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Represents a dictionary item, storing information about a word and its definitions.
 */
@Entity(tableName = "word_definitions")
public class DictionaryItem extends ArrayList<DictionaryItem> {
    /**
     * The auto-generated identifier for the dictionary item.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /**
     * The word associated with the dictionary item.
     */
    @ColumnInfo(name = "word")
    protected String word;

    /**
     * The definitions of the word associated with the dictionary item.
     */
    @ColumnInfo(name = "definition")
    protected String definitions;

    /**
     * Constructs a new {@code DictionaryItem} with the given word and definitions.
     * @param word        The word associated with the dictionary item.
     * @param definitions The definitions of the word associated with the dictionary item.
     */
    public DictionaryItem(String word, String definitions) {
        this.word = word;
        this.definitions = definitions;
    }

    /**
     * Gets the auto-generated identifier of the dictionary item.
     * @return The identifier of the dictionary item.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the auto-generated identifier of the dictionary item.
     * @param id The identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the word associated with the dictionary item.
     * @return The word associated with the dictionary item.
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets the definitions of the word associated with the dictionary item.
     * @return The definitions of the word associated with the dictionary item.
     */
    public String getDefinitions() {
        return definitions;
    }
}
