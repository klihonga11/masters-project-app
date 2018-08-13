package mobi.publishing.recipes.models.database;

import java.io.Serializable;

/**
 * Created by HP on 7/17/2018.
 */

public class RecipeIngredientModel implements Serializable {
    private String id;
    private String name;
    private String description;

    private RecipeModel recipe;

    public RecipeIngredientModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeModel recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equalsIgnoreCase((String)obj);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
