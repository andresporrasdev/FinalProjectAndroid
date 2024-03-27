package algonquin.cst2335.finalprojectandroid;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
    public interface SongDao {
        @Query("SELECT * FROM song")
        List<Song> getAll();

        @Insert
        void insert(Song song);

        @Delete
        void delete(Song song);


    }
