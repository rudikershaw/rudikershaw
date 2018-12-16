package main.water.alarm;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Exception that indicates that an attempt to retrieve the status of a given water alarm could not find any details. */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class WaterAlarmStatusNotFoundException extends RuntimeException {
    /**
     * Constructor requiring UUID of requested alarm's details.
     * @param uuid the UUID of the alarm who's details could not be found.
     */
    WaterAlarmStatusNotFoundException(final String uuid) {
        super("The status of the water alarm with UUID '" + uuid + "' could not be found.");
    }
}
