package main.twitter;

import java.util.ArrayList;
import java.util.List;
import main.dynamics.entities.TwitterFollow;
import main.dynamics.repositories.TwitterFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** Service for managing twitter users to automatically follow. */
@Service
public class TwitterFollowService {

    /** Injected Twitter follow repository. */
    private final TwitterFollowRepository repository;

    /**
     * Constructor for dependency injection.
     *
     * @param followRepository injected twitter follow repository.
     */
    @Autowired
    public TwitterFollowService(final TwitterFollowRepository followRepository) {
       this.repository = followRepository;
    }

    /**
     * Get a page of automatically managed twitter follows that are not disabled.
     *
     * @param pageable the pageable.
     * @return a list of Twitter follows.
     */
    public List<TwitterFollow> getPageOfFollows(final Pageable pageable) {
        return repository.findAllByDisabledFalseOrderByDateDesc(pageable);
    }

    /**
     * Get all follows that are not disabled.
     *
     * @return a list of Twitter follows.
     */
    public List<TwitterFollow> getFollows() {
        return new ArrayList<>(repository.findAllByDisabledFalse());
    }

    /**
     * Get the latest follow.
     *
     * @return get the latest follow.
     */
    public TwitterFollow getLatestFollow() {
        final List<TwitterFollow> follows = repository.findAllByDisabledFalseOrderByDateDesc(new PageRequest(0, 1));
        return follows.isEmpty() ? null : follows.get(0);
    }

    /**
     * Get a twitter follow by their username.
     *
     * @param name the username of the Twitter user to find amongst follows.
     * @return the Twitter follow.
     */
    public TwitterFollow getFollowByName(final String name) {
        return repository.findByName(name);
    }

    /**
     * Add a new Twitter follow.
     *
     * @param follow the follow to add.
     */
    public void addFollow(final TwitterFollow follow) {
        repository.save(follow);
    }

    /**
     * Disable the Twitter follow.
     *
     * @param follow the follow to disable.
     */
    public void disableFollow(final TwitterFollow follow) {
        follow.setDisabled(true);
        repository.save(follow);
    }
}
