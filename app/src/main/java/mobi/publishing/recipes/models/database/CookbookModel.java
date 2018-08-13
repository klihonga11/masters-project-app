package mobi.publishing.recipes.models.database;

import java.io.Serializable;

/**
 * Created by HP on 7/3/2018.
 */

public class CookbookModel implements Serializable {
    private String id;
    private String authorId;
    private String title;
    private String description;
    private boolean isPublished;
    private String publicationDate;
    private String imageUrl;

    public CookbookModel() {

    }

    public CookbookModel(String title, String description, boolean isPublished, String publicationDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.isPublished = isPublished;
        this.publicationDate = publicationDate;
        this.imageUrl = imageUrl;
    }

    public CookbookModel(String id, String title, String description, boolean isPublished, String publicationDate, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isPublished = isPublished;
        this.publicationDate = publicationDate;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
}
