package main.dynamics;

import main.dynamics.entities.Article;
import main.dynamics.entities.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Rudi Kershaw on 16/08/2015.
 */
@Service
public class ArticleService {

    ArticleRepository articleRepository;
    ArticleSessionService sessionService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ArticleSessionService sessionService){
       this.articleRepository = articleRepository;
        this.sessionService = sessionService;
    }

    public Article initialise(String articlePath, String articleName, String imagePath, String description, HttpServletRequest request){
        Article article = articleRepository.findByPath(articlePath);
        if(article == null){
            article = articleRepository.save(new Article(articleName, articlePath, imagePath, description));
            sessionService.isUniqueSession(request.getSession(), article.getId());
            return article;
        } else {
            if (sessionService.isUniqueSession(request.getSession(), article.getId())){
                article.setName(articleName);
                article.setImagePath(imagePath);
                article.setDescription(description);
                article.setViews(article.getViews()+1);
                articleRepository.save(article);
            }
            return article;
        }
    }

    public List<Article> getPageOfArticles(Pageable pageable){
        return articleRepository.findAllByOrderByPublishedDesc(pageable);
    }

    public Article getMostViewedThisWeek(){
        return articleRepository.findOne(sessionService.getMostSessionsThisWeekByArticleId());
    }
}
