package main.dynamics;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import main.dynamics.entities.Article;
import main.dynamics.repositories.ArticleRepository;

/** Article service. */
@Service
public class ArticleService {

    /** 2 hours as a number of milliseconds. */
    private static final int TWO_HOURS = 72000000;

    /** Article repo. */
    private final ArticleRepository articleRepository;

    /** Session service. */
    private final ArticleSessionService articleSessionService;

    /**
     * Constructor used for autowiring.
     * @param repository autowired article repo.
     * @param sessionService autowired session service.
     */
    @Autowired
    public ArticleService(final ArticleRepository repository, final ArticleSessionService sessionService) {
       this.articleRepository = repository;
        this.articleSessionService = sessionService;
    }

    /**
     * Initialise an article, store in the DB if not already there, and increment unique views if already there.
     * @param articlePath the path of the article.
     * @param articleName the name of the article.
     * @param imagePath the image path of the article.
     * @param description the description of the article.
     * @param request the request for this article to check for uniqueness of view.
     * @return the initialised article.
     */
    public Article initialise(final String articlePath, final String articleName, final String imagePath,
                              final String description, final HttpServletRequest request) {
        Article article = articleRepository.findByPath(articlePath);
        if (article == null) {
            article = articleRepository.save(new Article(articleName, articlePath, imagePath, description));
            articleSessionService.isUniqueSession(request, article);
        } else if (articleSessionService.isUniqueSession(request, article)) {
            article.setName(articleName);
            article.setImagePath(imagePath);
            article.setDescription(description);
            article.setViews(article.getViews() + 1);
            articleRepository.save(article);
        }
        return article;
    }

    /**
     * Get the article which has been viewed most this week or a default if none have been viewed.
     * @return the article which has been viewed most this week or a default if none have been viewed.
     */
    @Cacheable("most-viewed-article")
    public Article getMostViewedThisWeek() {
        final Integer id = articleSessionService.getMostSessionsThisWeekByArticleId();
        if (id == null) {
            return new Article("No Articles Viewed", "/", "/images/RK.png",
                                "Apparently no one has viewed ANY of my articles over the last 7 days. Come on people!");
        } else {
            return articleRepository.findById(id).orElse(null);
        }
    }

    /** Evict entries from the Latest Tweet cache every 2 hours. */
    @CacheEvict(allEntries = true, cacheNames = { "most-viewed-article" })
    @Scheduled(fixedDelay = TWO_HOURS)
    public void invalidateCache() {
        // The effect of this method is purely in the annotations.
    }
}
