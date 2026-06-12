package main.twitter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AuthDetailsProviderTest {

    @Test
    public void testDetailAvailableReturnsFalseWhenFallback() {
        final AuthDetailsProvider provider = new AuthDetailsProvider(
            "fallback", "fallback", "fallback", "fallback");
        assertThat(provider.detailAvailable()).isFalse();
    }

    @Test
    public void testDetailAvailableReturnsTrueWhenRealValues() {
        final AuthDetailsProvider provider = new AuthDetailsProvider(
            "real-consumer-key", "real-consumer-secret", "real-access-key", "real-access-secret");
        assertThat(provider.detailAvailable()).isTrue();
    }

    @Test
    public void testDetailAvailableReturnsFalseWhenOneFallback() {
        final AuthDetailsProvider provider = new AuthDetailsProvider(
            "real-key", "fallback", "real-access-key", "real-access-secret");
        assertThat(provider.detailAvailable()).isFalse();
    }

    @Test
    public void testGetAccessTokenReturnsTokenWithCorrectKeys() {
        final AuthDetailsProvider provider = new AuthDetailsProvider(
            "consumer-key", "consumer-secret", "access-key", "access-secret");
        final var token = provider.getAccessToken();
        assertThat(token).isNotNull();
    }
}
