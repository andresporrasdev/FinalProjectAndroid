/*
 * Purpose: This file defines the LocationEntity class which represents a location record in the database.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid.sslookup;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Represents a location entity in the database with fields for ID, latitude, longitude, and a formatted location string.
 */
@Entity(tableName = "locations")
public class LocationEntity {
    /**
     * Primary key for the location entity, automatically generated.
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * Latitude coordinate of the location.
     */
    public double latitude;

    /**
     * Longitude coordinate of the location.
     */
    public double longitude;

    /**
     * Readable formatted location name.
     */
    public String formattedLocation;
}


