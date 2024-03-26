package algonquin.cst2335.finalprojectandroid.sslookup;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public double latitude;
    public double longitude;
    public String formattedLocation;
}


