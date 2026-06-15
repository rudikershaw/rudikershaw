package main.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** Utility for reading the latest date from the bibliography JSON file. */
public final class BibliographyDateUtils {

    private BibliographyDateUtils() {
        // utility class
    }

    /**
     * Returns the most recent {@code dateFinished} value from the bibliography file as a {@link Date}.
     * Falls back to a default date if the file is unreadable or empty.
     *
     * @return the latest date found, or a fallback date if the file cannot be read.
     */
    public static Date getLatestDate() {
        try (var stream = StaticResources.get("data/bibliography.json")) {
            if (stream == null) {
                return getDefaultDate();
            }

            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode root = mapper.readTree(stream);
            final JsonNode entries = root.get("bibliography");

            if (entries == null || !entries.isArray()) {
                return getDefaultDate();
            }

            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date latest = null;

            for (final JsonNode entry : entries) {
                final JsonNode dateNode = entry.get("dateFinished");
                if (dateNode != null && !dateNode.isNull()) {
                    final Date date = dateFormat.parse(dateNode.asText());
                    if (latest == null || date.after(latest)) {
                        latest = date;
                    }
                }
            }

            return latest != null ? latest : getDefaultDate();
        } catch (IOException | ParseException e) {
            return getDefaultDate();
        }
    }

    /**
     * Returns a fallback date roughly 6 months ago from now.
     *
     * @return a date 6 months in the past.
     */
    private static Date getDefaultDate() {
        final Calendar calendar = Calendar.getInstance();
        final int monthsPrior = -6;
        calendar.add(Calendar.MONTH, monthsPrior);
        return calendar.getTime();
    }
}
