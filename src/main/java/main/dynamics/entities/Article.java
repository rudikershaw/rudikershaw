package main.dynamics.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import main.StaticResources;

/** An article entity representing an article and it's url as well as any statistics against it. */
@Entity
public class Article implements Serializable {

    /** The logger for the article class. */
    private static final Logger LOGGER = Logger.getLogger(Article.class.getName());

    /** The ID for this article. */
    private Integer id;

    /** The path as an identifier, same as the url to view the article. */
    @Column(unique = true)
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
    public Article() {
        super();
    }

    /**
     * Constructor for a new article.
     * @param articleName the name of the article.
     * @param articlePath the path to the article.
     * @param image the path to the image for this article.
     * @param desc short description of the article for summaries etc.
     */
    public Article(final String articleName, final String articlePath, final String image, final String desc) {
        this.name = articleName;
        this.path = articlePath;
        this.imagePath = image;
        this.description = desc;
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

        try (InputStream is = StaticResources.get(largeImagePath)) {
            if (is != null) {
                return largeImagePath;
            }
        } catch (final IOException ioe) {
            LOGGER.warning(ioe.toString());
        }

        return getImagePath();
    }

    /**
     * Get the ID for this entity.
     * @return this entities ID.
     */
    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer getId() {
        return id;
    }

    /**
     * Set the ID for this entity.
     * @param identifier an integer ID.
     */
    public void setId(final Integer identifier) {
        this.id = identifier;
    }

    /**
     * Get the path for this article's view.
     * @return the article view path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the path to the article's view.
     * @param articlePath the path to the article's view.
     */
    public void setPath(final String articlePath) {
        this.path = articlePath;
    }

    /**
     * Get the number of views for this article.
     * @return the number of views for this article.
     */
    public Integer getViews() {
        return views;
    }

    /**
     * Set the number od article views.
     * @param articleViews the number of article view.
     */
    public void setViews(final Integer articleViews) {
        this.views = articleViews;
    }

    /**
     * The name of this article.
     * @return the name of this article.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this article.
     * @param articleName the article name.
     */
    public void setName(final String articleName) {
        this.name = articleName;
    }

    /**
     * Get the date this article was published (initialised).
     * @return the date this article was published.
     */
    public Date getPublished() {
        return new Date(published.getTime());
    }

    /**
     * Set the date that this article was published (usually set when initialised).
     * @param publishedDate the date this article was published.
     */
    public void setPublished(final Date publishedDate) {
        this.published = publishedDate == null ? null : new Date(publishedDate.getTime());
    }

    /**
     * Get the path to this article's image.
     * @return the path to this article's image.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Set the path to this article's image.
     * @param image the path to this article's image.
     */
    public void setImagePath(final String image) {
        this.imagePath = image;
    }

    /**
     * Get the short description or summary of this article.
     * @return the short description of summary of this article.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the short description or summary of this article.
     * @param desc the description.
     */
    public void setDescription(final String desc) {
        this.description = desc;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
