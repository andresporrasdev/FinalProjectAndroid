package algonquin.cst2335.finalprojectandroid;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipeId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "summary")
    public String summary;

    @ColumnInfo(name = "sourceUrl")
    public String sourceUrl;

    public Recipe() {

    }

    public Recipe(int r, String t, String i, String s, String u) {
        recipeId = r;
        title = t;
        image = i;
        summary = s;
        sourceUrl = u;
    }

    public Recipe(int r, String t, String i) {
        recipeId = r;
        title = t;
        image = i;
    }

    public Recipe(String i, String s, String u) {
        image = i;
        summary = s;
        sourceUrl = u;
    }

    public int getRecipeId(){return recipeId;}
    public String getTitle(){return title;}
    public String getImage(){return image;}
    public String getSummary(){return summary;}
    public String getSourceUrl(){return sourceUrl;}

}
