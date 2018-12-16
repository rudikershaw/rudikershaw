package main.water.alarm;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST web controller for setting and querying recent status updates from an external water alarm. */
@RestController
public class WaterAlarmRestController {

    /** Twitter API access secret. */
    @Value("${maximum.water.alarms}")
    private short maximumSize;

    /** Status sent and updated from an external water alarm. */
    private Map<String, WaterStatus> statuses = new HashMap<>();

    /**
     * Update recent status of the water alarm.
     *
     * @param uuid the UUID of the alarm who's status you want to update.
     * @param water whether water was detected.
     * @return the status of the rest call.
     */
    @RequestMapping("/water-alarm/update/{uuid}/{water}")
    public WaterStatus updateStatus(@PathVariable final String uuid, @PathVariable final boolean water) {
        WaterStatus status = statuses.get(uuid);
        if (status == null) {
            status = new WaterStatus(uuid);
            status.setWater(water);
            // Limit the size of the map, in case of DOS attack.
            if (statuses.size() < maximumSize) {
                statuses.put(uuid, status);
            }
        } else if (status.isWater() != water) {
            status.setLastUpdate(ZonedDateTime.now());
            status.setWater(water);
        }
        status.setLastContact(ZonedDateTime.now());
        return status;
    }

    /**
     * Returns a JSON or XML response describing the status of the external water alarm.
     *
     * @param uuid the UUID of the alarm who's status you want to query.
     * @return a water status object.
     */
    @RequestMapping("/water-alarm/query/{uuid}")
    public WaterStatus query(@PathVariable final String uuid) {
        final WaterStatus status = statuses.get(uuid);
        if (status == null) {
            throw new WaterAlarmStatusNotFoundException(uuid);
        }
        return status;
    }
}
