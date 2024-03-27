package algonquin.cst2335.finalprojectandroid;
public class Song {
    private String id;
    private String title;
    private String duration;
    private String albumName;
    private String albumCoverUrl;

    public Song( String title, String duration, String albumName, String albumCoverUrl) {
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCoverUrl = albumCoverUrl;
    }
    public void setId(String id) {
        this.id = id;
    }
    // Getters
    public String getId() {
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