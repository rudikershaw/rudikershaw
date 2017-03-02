package main.dynamics;

import java.util.ArrayList;
import java.util.List;
import main.dynamics.entities.TwitterFollow;
import main.dynamics.repositories.TwitterFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Rudi Kershaw on 03/03/2017.
 */
@Service
public class TwitterFollowService {

    private TwitterFollowRepository repository;

    @Autowired
    public TwitterFollowService(TwitterFollowRepository repository){
       this.repository = repository;
    }

    public List<TwitterFollow> getPageOfFollows(Pageable pageable){
        return repository.findAllByDisabledFalseOrderByDateDesc(pageable);
    }

    public List<TwitterFollow> getFollows(){
        ArrayList<TwitterFollow> follows = new ArrayList<>();
        repository.findAllByDisabledFalse().forEach(follows::add);
        return follows;
    }

    public TwitterFollow getLatestFollow(){
        List<TwitterFollow> follows = repository.findAllByDisabledFalseOrderByDateDesc(new PageRequest(0, 1));
        return follows.isEmpty() ? null : follows.get(0);
    }

    public TwitterFollow getFollowByName(String name){
        return repository.findByName(name);
    }

    public void addFollow(TwitterFollow follow){
        repository.save(follow);
    }

    public void disableFollow(TwitterFollow follow){
        follow.setDisabled(true);
        repository.save(follow);
    }
}
