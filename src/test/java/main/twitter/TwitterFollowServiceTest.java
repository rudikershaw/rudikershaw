package main.twitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import main.dynamics.entities.TwitterFollow;
import main.dynamics.repositories.TwitterFollowRepository;

@ExtendWith(MockitoExtension.class)
public class TwitterFollowServiceTest {

    @Mock
    private TwitterFollowRepository repository;

    @InjectMocks
    private TwitterFollowService service;

    @Test
    public void testGetPageOfFollowsReturnsFollows() {
        final TwitterFollow follow = new TwitterFollow("user1", new Date());
        final Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAllByDisabledFalseOrderByDateDesc(pageable)).thenReturn(List.of(follow));

        final List<TwitterFollow> result = service.getPageOfFollows(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("user1");
    }

    @Test
    public void testGetFollowsReturnsAllEnabled() {
        final TwitterFollow follow = new TwitterFollow("user1", new Date());

        when(repository.findAllByDisabledFalse()).thenReturn(List.of(follow));

        final List<TwitterFollow> result = service.getFollows();

        assertThat(result).hasSize(1);
    }

    @Test
    public void testGetLatestFollowReturnsLatest() {
        final TwitterFollow follow = new TwitterFollow("user1", new Date());

        when(repository.findAllByDisabledFalseOrderByDateDesc(PageRequest.of(0, 1))).thenReturn(List.of(follow));

        final TwitterFollow result = service.getLatestFollow();

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("user1");
    }

    @Test
    public void testGetLatestFollowReturnsNullWhenEmpty() {
        when(repository.findAllByDisabledFalseOrderByDateDesc(PageRequest.of(0, 1))).thenReturn(List.of());

        final TwitterFollow result = service.getLatestFollow();

        assertThat(result).isNull();
    }

    @Test
    public void testGetFollowByNameReturnsFollow() {
        final TwitterFollow follow = new TwitterFollow("user1", new Date());

        when(repository.findByName("user1")).thenReturn(follow);

        final TwitterFollow result = service.getFollowByName("user1");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("user1");
    }

    @Test
    public void testAddFollowSaves() {
        final TwitterFollow follow = new TwitterFollow("user1", new Date());

        service.addFollow(follow);

        verify(repository).save(follow);
    }

    @Test
    public void testDisableFollowSetsDisabledAndSaves() {
        final TwitterFollow follow = new TwitterFollow("user1", new Date());

        service.disableFollow(follow);

        assertThat(follow.isDisabled()).isTrue();
        verify(repository).save(follow);
    }
}
