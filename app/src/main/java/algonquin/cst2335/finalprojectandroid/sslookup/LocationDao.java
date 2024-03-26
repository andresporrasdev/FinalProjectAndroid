package algonquin.cst2335.finalprojectandroid.sslookup;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(LocationEntity location);

    @Query("SELECT * FROM locations")
    List<LocationEntity> getAllLocations();

    @Delete
    void deleteLocation(LocationEntity location);

    @Query("SELECT * FROM locations WHERE id = :id")
    LocationEntity getLocationById(int id);

}

