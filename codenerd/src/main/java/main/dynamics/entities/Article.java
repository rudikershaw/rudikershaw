package main.dynamics.entities;

import javax.persistence.*;

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

    /** No-args constructor. */
    public Article(){
        super();
    }

    /** Constructor for a new article. */
    public Article(String name, String path){
        this.name = name;
        this.path = path;
        views = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
