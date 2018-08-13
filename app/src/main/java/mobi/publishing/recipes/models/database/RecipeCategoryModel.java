package mobi.publishing.recipes.models.database;

import java.io.Serializable;

/**
 * Created by HP on 7/11/2018.
 */

public class RecipeCategoryModel implements Serializable {
    private String id;
    private String name;
    private String description;

    private CookbookModel cookbook;

    public RecipeCategoryModel() {

    }

    public RecipeCategoryModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RecipeCategoryModel(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public CookbookModel getCookbook() {
        return cookbook;
    }

    public void setCookbook(CookbookModel cookbook) {
        this.cookbook = cookbook;
    }
}
