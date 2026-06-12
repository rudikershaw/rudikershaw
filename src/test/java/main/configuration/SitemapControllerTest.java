package main.configuration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.TestPropertySource;

@WebMvcTest(SitemapController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {"use.ssl=false", "base.url=https://www.rudikershaw.com/"})
public class SitemapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSitemapReturnsXml() throws Exception {
        mockMvc.perform(get("/sitemap.xml"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/xml"));
    }
}
