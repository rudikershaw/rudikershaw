package main.dynamics.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * An entity representing someone the site owner follows on
 * twitter when the follow was created automatically by the site.
 */
@Entity
public class TwitterFollow {

    /** The ID for this entity. */
    private Integer id;

    /** The name of the account being followed. */
    private String name;

    /** The date that this person was followed initially. */
    private Date date;

    /** True if you have followed this person previously but no longer. */
    private boolean disabled;

    /** No-args constructor. */
    public TwitterFollow() {
        super();
    }

    /**
     * Constructor taking the name and follow date of this follow.
     * @param userName the name of the user followed.
     * @param followDate the date of this follow.
     */
    public TwitterFollow(final String userName, final Date followDate) {
        this.name = userName;
        this.date = followDate == null ? null : new Date(followDate.getTime());
    }

    /**
     * Gets the ID of this entity.
     * @return the ID.
     */
    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of this entity.
     * @param identifier the identifier.
     */
    public void setId(final Integer identifier) {
        this.id = identifier;
    }

    /**
     * Gets the name of the user followed.
     * @return the users name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user followed.
     * @param userName the name of the user.
     */
    public void setName(final String userName) {
        this.name = userName;
    }

    /**
     * Get the date that this Twitter user was followed.
     * @return the date this user was followed.
     */
    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    /**
     * Sets the date that this Twitter user was followed.
     * @param followDate the follow date.
     */
    public void setDate(final Date followDate) {
        this.date = followDate == null ? null : new Date(followDate.getTime());
    }

    /**
     * Checks whether this twitter follow is disabled.
     * @return whether disabled.
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Sets whether this TwitterFollow has been disabled.
     * @param disable boolean whether disabled.
     */
    public void setDisabled(final boolean disable) {
        this.disabled = disable;
    }
}
