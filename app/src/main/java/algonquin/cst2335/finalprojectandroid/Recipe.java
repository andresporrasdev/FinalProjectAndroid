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
    public String recipeId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "image")
    public String image;

    public Recipe() {

    }

    public Recipe(String r, String t, String i) {
        recipeId = r;
        title = t;
        image = i;
    }

    public String getRecipeId(){return recipeId;}
    public String getTitle(){return title;}
    public String getImage(){return image;}

}
