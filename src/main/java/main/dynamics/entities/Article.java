package main.dynamics.entities;

import javax.persistence.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import main.StaticResources;

/** An article entity representing an article and it's url as well as any statistics against it. */
@Entity
public class Article {

    private Integer id;
    /** The path as an identifier, same as the url to view the article. */
    @Column(unique=true)
    private String path;
    /** The number of (hopefully) unique views of this article. */
    private Integer views;
    /** The article's name or title. */
    private String name;
    /** The article's thumbnail path. */
    private String imagePath;
    /** The article's description. */
    private String description;

    /** Date that the article was first viewed/published. */
    @Temporal(TemporalType.DATE)
    private Date published;

    /** No-args constructor. */
    public Article(){
        super();
    }

    /** Constructor for a new article. */
    public Article(String name, String path, String imagePath, String description){
        this.name = name;
        this.path = path;
        this.imagePath = imagePath;
        this.description = description;
        views = 1;
        published = new Date();
    }

    /**
     * Get the path for a larger version of the article image if one exists.
     * If no larger image exists, returns the path to the standard image.
     *
     * @return the path of a large version of the image or the standard image path otherwise.
     */
    @Transient
    public String getLargeImagePath() {
        final String extension = imagePath.substring(imagePath.lastIndexOf('.'));
        final String pathMinusExtension = imagePath.substring(0, imagePath.lastIndexOf('.'));
        final String largeImagePath = pathMinusExtension + "-large" + extension;

        try (final InputStream is = StaticResources.get(largeImagePath)) {
            if (is != null) {
                return largeImagePath;
            }
        } catch (IOException ioe) {
            // Do nothing.
        }

        return getImagePath();
    }

    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublished() {
        return new Date(published.getTime());
    }

    public void setPublished(Date published) {
        this.published = published == null ? null : new Date(published.getTime());
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
