package algonquin.cst2335.finalprojectandroid;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="duration")
    private String duration;
    @ColumnInfo(name="albumName")
    private String albumName;
    @ColumnInfo(name="albumCoverUrl")
    private String albumCoverUrl;

    public Song( String title, String duration, String albumName, String albumCoverUrl) {
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }
}