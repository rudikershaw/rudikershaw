package main.dynamics.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/** An entity representing a label / description pair entry for the whichpet data-set. */
@Entity
public class WhichpetDatum {

    private Integer id;
    /** The label corresponding to this data entry's description. */
    private String label;
    /** The description of the abstract thing characterised by the label. */
    private String description;

    /** No-args constructor. */
    public WhichpetDatum(){
        super();
    }

    /** Constructor for a new entry of the whichpet data set. */
    public WhichpetDatum(String label, String description){
        this.label = label;
        this.description = description;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
