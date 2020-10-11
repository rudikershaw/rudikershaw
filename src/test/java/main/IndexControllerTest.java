package main;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndexPageWithNoArticles() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No Articles Viewed")));
    }

    @Test
    public void testLatestTweet() throws Exception {
        mockMvc.perform(get("/latest-tweet")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("twitter.com/RudiKershaw/status")));
    }
}
