package main.water.alarm;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/** A POJO representing the status being reported by a water alarm. */
public class WaterStatus {

    /** Universally unique ID for the water alarm this status belongs to. */
    private String uuid;

    /** Whether the last update received from the water alarm detected water. */
    private boolean water;

    /** The date the last update was made to whether water was detected. */
    private ZonedDateTime lastUpdate = ZonedDateTime.now(ZoneId.of("GMT"));

    /** The date of the last contact received from the water alarm. */
    private ZonedDateTime lastContact = ZonedDateTime.now(ZoneId.of("GMT"));

    /** Default constructor for POJO compliance. */
    public WaterStatus() {
        super();
    }

    /**
     * Constructor taking alarm UUID.
     *
     * @param alarmUuid UUID of the alarm this status belongs to.
     */
    public WaterStatus(final String alarmUuid) {
        uuid = alarmUuid;
    }

    /**
     * Get whether the last update received from the water alarm detected water.
     *
     * @return whether water has been detected.
     */
    public boolean isWater() {
        return water;
    }

    /**
     * Sets whether the last update received from the water alarm detected water.
     *
     * @param waterUpdate whether water has been detected..
     */
    public void setWater(final boolean waterUpdate) {
        this.water = waterUpdate;
    }

    /**
     * Gets the date the last update was made to whether water was detected.
     *
     * @return the last update date.
     */
    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the date the last update was made to whether water was detected.
     *
     * @param lastUpdateDate the last update date.
     */
    public void setLastUpdate(final ZonedDateTime lastUpdateDate) {
        this.lastUpdate = lastUpdateDate;
    }

    /**
     * Gets the date of the last contact received from the water alarm.
     *
     * @return the last contact date.
     */
    public ZonedDateTime getLastContact() {
        return lastContact;
    }

    /**
     * Sets the date of the last contact received from the water alarm..
     *
     * @param lastContactDate the last contact date.
     */
    public void setLastContact(final ZonedDateTime lastContactDate) {
        this.lastContact = lastContactDate;
    }

    /**
     * Gets the UUID of the water alarm this status belongs to.
     *
     * @return the UUID.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of the water alarm this status belongs to.
     *
     * @param alarmUuid the UUID.
     */
    public void setUuid(final String alarmUuid) {
        this.uuid = alarmUuid;
    }
}
