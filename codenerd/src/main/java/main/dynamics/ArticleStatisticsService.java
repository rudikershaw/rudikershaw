package main.dynamics;

import main.dynamics.entities.Article;
import main.dynamics.entities.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Rudi Kershaw on 16/08/2015.
 */
@Service
public class ArticleStatisticsService {

    ArticleRepository articleRepository;
    ArticleSessionService sessionService;

    @Autowired
    public ArticleStatisticsService(ArticleRepository articleRepository, ArticleSessionService sessionService){
       this.articleRepository = articleRepository;
        this.sessionService = sessionService;
    }

    public Integer incrementViews(String articlePath, String articleName, HttpServletRequest request){
        Article article = articleRepository.findByPath(articlePath);
        if(article == null){
            article = articleRepository.save(new Article(articleName, articlePath));
            sessionService.isUniqueSession(request.getSession(), article.getId());
            return 1;
        } else {
            if (sessionService.isUniqueSession(request.getSession(), article.getId())){
                article.setViews(article.getViews()+1);
                articleRepository.save(article);
            }
            return article.getViews();
        }
    }

    public List<Article> getAllArticles()
    {
        return articleRepository.findAll();
    }
}
