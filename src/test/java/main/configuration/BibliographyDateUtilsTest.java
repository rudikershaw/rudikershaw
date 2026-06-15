package main.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class BibliographyDateUtilsTest {

    @Test
    public void testGetLatestDateReturnsRecentDate() {
        final Date latest = BibliographyDateUtils.getLatestDate();
        assertThat(latest).isNotNull();
        // The latest date should be recent (within the last year)
        final Date oneYearAgo = new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000);
        assertThat(latest.after(oneYearAgo)).isTrue();
    }

    @Test
    public void testGetLatestDateNeverReturnsNull() {
        // Even if the file is unreadable, the fallback should kick in
        final Date result = BibliographyDateUtils.getLatestDate();
        assertThat(result).isNotNull();
    }
}
