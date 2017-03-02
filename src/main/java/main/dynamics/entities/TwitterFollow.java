package main.dynamics.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * An entity representing someone the site owner follows on
 * twitter when the follow was created automatically by the site.
 */
@Entity
public class TwitterFollow {

    private Integer id;
    /** The name of the account being followed. */
    private String name;
    /** The date that this person was followed initially. */
    private Date date;
    /** True if you have followed this person previously but no longer. */
    private boolean disabled;

    /** No-args constructor. */
    public TwitterFollow(){
        super();
    }

    /** Constructor for a new entry of the whichpet data set. */
    public TwitterFollow(String name, Date date){
        this.name = name;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date == null ? null : new Date(date.getTime());
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
