package main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

import main.dynamics.ArticleService;
import main.dynamics.entities.Article;
import main.reddit.RedditService;
import main.twitter.TwitterService;

@WebMvcTest(IndexController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "use.ssl=false")
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private TwitterService twitterService;

    @MockitoBean
    private RedditService redditService;

    @BeforeEach
    void setUp() {
        when(articleService.getMostViewedThisWeek()).thenReturn(new Article("Test", "test", "img.png", null, "desc"));
    }

    @Test
    public void testIndexReturnsCorrectView() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }
}
