package main.dynamics;

import main.dynamics.entities.Article;
import main.dynamics.entities.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Rudi Kershaw on 16/08/2015.
 */
@Service
public class ArticleStatisticsService {

    ArticleRepository articleRepository;

    @Autowired
    public ArticleStatisticsService(ArticleRepository articleRepository){
       this.articleRepository = articleRepository;
    }

    public Integer incrementViews(String articlePath, String articleName){
        Article article = articleRepository.findByPath(articlePath);
        if(article == null){
            articleRepository.save(new Article(articleName, articlePath));
            return 1;
        } else {
            article.setViews(article.getViews()+1);
            articleRepository.save(article);
            return article.getViews();
        }
    }

    public List<Article> getAllArticles()
    {
        return articleRepository.findAll();
    }
}
