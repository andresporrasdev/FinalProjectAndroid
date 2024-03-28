package algonquin.cst2335.finalprojectandroid.sslookup;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LocationEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}

