package mobi.publishing.recipes.models.database;

import java.io.Serializable;

/**
 * Created by HP on 7/11/2018.
 */

public class RecipeModel implements Serializable {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String difficulty;
    private int numberOfServings;

    private RecipeCategoryModel recipeCategory;

    public RecipeModel() {

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public RecipeCategoryModel getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(RecipeCategoryModel recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
