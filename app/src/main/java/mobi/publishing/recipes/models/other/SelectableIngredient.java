package mobi.publishing.recipes.models.other;

import java.io.Serializable;

/**
 * Created by HP on 6/14/2018.
 */

public class SelectableIngredient implements Serializable {
    private boolean selected;
    private String name;

    public SelectableIngredient() {

    }

    public SelectableIngredient(String name) {
        this.selected = true;
        this.name = name;
    }

    public SelectableIngredient(boolean selected, String name) {
        this.selected = selected;
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equalsIgnoreCase(((SelectableIngredient)obj).getName());
    }
}
