package main.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.mockStatic;

public class BibliographyDateUtilsTest {

    @Test
    public void testGetLatestDateReturnsRecentDate() throws ParseException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date expectedDate = dateFormat.parse("2026-05-07");

        final Date latest = BibliographyDateUtils.getLatestDate();
        
        assertThat(latest).isNotNull();
        assertThat(latest).isEqualTo(expectedDate);
    }

    @Test
    public void testGetLatestDateFallbackWhenFileUnreadable() {
        try (var mock = mockStatic(StaticResources.class)) {
            mock.when(() -> StaticResources.get("data/bibliography.json"))
                    .thenReturn(null);
            final Date oneYearAgo = new Date(System.currentTimeMillis() - 365L * 24 * 60 * 60 * 1000);

            final Date result = BibliographyDateUtils.getLatestDate();

            assertThat(result).isNotNull();
            assertThat(result.before(new Date())).isTrue();
            assertThat(result.after(oneYearAgo)).isTrue();
        }
    }
}
