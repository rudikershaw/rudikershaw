package main.dynamics.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import main.dynamics.entities.TwitterFollow;

@DataJpaTest
public class TwitterFollowRepositoryTest {

    @Autowired
    private TwitterFollowRepository followRepository;

    @Test
    public void testTwitterFollowCrud() {
        final TwitterFollow follow = createTwitterFollow();
        followRepository.save(follow);

        final TwitterFollow loadedFollow = followRepository.findByName(follow.getName());

        assertThat(loadedFollow).isEqualTo(follow);
    }

    private TwitterFollow createTwitterFollow() {
        return new TwitterFollow("twitteruser29", new Date());
    }
}
