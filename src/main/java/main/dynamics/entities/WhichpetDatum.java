package main.dynamics.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/** An entity representing a label / description pair entry for the whichpet data-set. */
@Entity
public class WhichpetDatum {

    /** The entity ID. */
    private Integer id;

    /** The label corresponding to this data entry's description. */
    private String label;

    /** The description of the abstract thing characterised by the label. */
    private String description;

    /** No-args constructor. */
    public WhichpetDatum() {
        super();
    }

    /**
     * Constructor for a new entry of the whichpet data set.
     * @param dataLabel the label for the description.
     * @param dataDescription the description.
     */
    public WhichpetDatum(final String dataLabel, final String dataDescription) {
        this.label = dataLabel;
        this.description = dataDescription;
    }

    /**
     * The ID for this entity.
     * @return the entity ID.
     */
    @Id
    @GenericGenerator(name = "native", strategy = "native")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    public Integer getId() {
        return id;
    }

    /**
     * Set the ID for this entity.
     * @param identifier the ID.
     */
    public void setId(final Integer identifier) {
        this.id = identifier;
    }

    /**
     * Set the label for the description in this datum.
     * @return the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the label for the description in this datum.
     * @param dataLabel the label.
     */
    public void setLabel(final String dataLabel) {
        this.label = dataLabel;
    }

    /**
     * Get the description in this datum.
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description in this datum.
     * @param desc the description.
     */
    public void setDescription(final String desc) {
        this.description = desc;
    }
}
