package main;

import static main.articles.DefaultArticleController.ARTICLES;

import java.net.MalformedURLException;
import java.util.Date;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import main.dynamics.entities.Article;

/** Controller that serves a sitemap.xml for search-engine discovery. */
@Controller
public class SitemapController {

    /** Base URL from the properties file. */
    @Value("${base.url}")
    private String baseUrl;

    /**
     * Generate and return the site's sitemap.xml.
     *
     * @return the XML body with the correct content type.
     * @throws MalformedURLException if the configured base URL is invalid.
     */
    @Cacheable("sitemap")
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() throws MalformedURLException {
        final WebSitemapGenerator generator = new WebSitemapGenerator(baseUrl);

        generator.addUrl(new WebSitemapUrl.Options(baseUrl)
                .changeFreq(ChangeFreq.DAILY)
                .lastMod(new Date())
                .priority(1.0)
                .build());

        final double secondPriority = 0.8;

        generator.addUrl(new WebSitemapUrl.Options(baseUrl + "bibliography")
                .changeFreq(ChangeFreq.MONTHLY)
                .priority(secondPriority)
                .build());

        for (final Article article : ARTICLES) {
            generator.addUrl(new WebSitemapUrl.Options(baseUrl + article.getPath())
                    .lastMod(article.getPublished())
                    .changeFreq(ChangeFreq.YEARLY)
                    .priority(secondPriority)
                    .build());
        }

        final String xml = generator.writeAsStrings().stream().findFirst().orElseThrow();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(xml);
    }
}
